package com.test.transport.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import cds.gen.com.test.transport.transportationservice.TransportationOrderItem_;

import static cds.gen.com.test.transport.transportationservice.TransportationService_.TRANSPORTATION_ORDER_ITEM;


import cds.gen.com.test.transport.transportationservice.TransportationOrderService;
import cds.gen.com.test.transport.transportationservice.TransportationOrderService_;
import cds.gen.com.test.transport.transportationservice.TransportationService;
import cds.gen.com.test.transport.transportationservice.TransportationService_;
import cds.gen.com.test.transport.transportationservice.TransportationOrderServiceSetToExecutionContext;
import cds.gen.com.test.transport.transportationservice.TransportationOrderServiceSetToPlanningContext;
import cds.gen.com.test.transport.TransportationOrderItem;
import com.sap.cds.Result;
import com.sap.cds.ql.*;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.*;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.messages.Messages;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.draft.DraftNewEventContext;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sap.cds.services.cds.CdsReadEventContext;

import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cds.gen.com.test.transport.TransportationOrder;
import cds.gen.com.test.transport.TransportationOrder_;
import cds.gen.com.test.transport.TransportationOrderItem;


@Component
@ServiceName(TransportationService_.CDS_NAME)
class CatalogServiceHandler implements EventHandler {
	private static final String STATUS_PLANNING = "inPlanning";
	private static final String STATUS_EXECUTION = "inExecution";

	private final PersistenceService db;
	private final CqnAnalyzer analyzer;

    public CatalogServiceHandler(PersistenceService db, CdsModel model) {
        this.db = db;
		this.analyzer = CqnAnalyzer.create(model);
    }

	@On(event = TransportationOrderServiceSetToExecutionContext.CDS_NAME, entity = TransportationOrderService_.CDS_NAME)
	public void setToExecution(TransportationOrderServiceSetToExecutionContext context) {
		try {
			String orderId = (String) analyzer.analyze(context.getCqn()).targetKeys().get(TransportationOrder.DISPLAY_ID);

			CqnSelect select = Select.from(TransportationOrderItem_.class)
					.where(t -> t.order_displayId().eq(orderId));

			Result result = db.run(select);

			if(!result.list().isEmpty()){
				Map<String, Object> map = new HashMap<>();
				map.put("editHide", "true");
				map.put("status", "inExecution");
				CqnUpdate update = Update.entity(TransportationOrder_.class)
						.where(t -> t.displayId().eq(orderId))
								.data(map);
				db.run(update);
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorStatuses.SERVER_ERROR, "Failed to update status: no items in Order", e);
		}
		context.setCompleted();

	}

	@On(event = TransportationOrderServiceSetToPlanningContext.CDS_NAME, entity = TransportationOrderService_.CDS_NAME)
	public void setToPlanning(TransportationOrderServiceSetToPlanningContext context) {
		try {
			String orderId = (String) analyzer.analyze(context.getCqn()).targetKeys().get(TransportationOrder.DISPLAY_ID);
         //   String status = (String) analyzer.analyze(context.getCqn()).targetKeys().get(TransportationOrder.STATUS);

            CqnSelect select = Select.from(TransportationOrder_.class)
                    .where(t -> t.displayId().eq(orderId))
					.columns(cds.gen.com.test.transport.TransportationOrder_::status);

            Result result = db.run(select);
			if("inExecution".equals(result.first().get().get("status"))){
				Map<String, Object> map = new HashMap<>();
				map.put("editHide", "false");
				map.put("status", "inPlanning");
				CqnUpdate update = Update.entity(TransportationOrder_.class)
						.where(t -> t.displayId().eq(orderId))
						.data(map);
				db.run(update);
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorStatuses.SERVER_ERROR, "Failed to update status", e);
		}

		context.setCompleted();

	}

	@After(event = CqnService.EVENT_READ, entity = TransportationOrderService_.CDS_NAME)//(event = CqnService.EVENT_READ)
	public void calculateTotalWeights(Stream<TransportationOrderService> orders) {

		if (orders != null) {

			orders.forEach(o -> {

				CqnSelect select = Select.from(TransportationOrderItem_.class)
						.where(t -> t.order_displayId().eq(o.getDisplayId()));

				Result result = db.run(select);

				if(result.list().isEmpty()){
					CqnUpdate update = Update.entity(TransportationOrder_.class)
							.where(t -> t.displayId().eq(o.getDisplayId()))
							.data("editHide", "true");
					db.run(update);
				}

				CqnSelect selectItem = Select.from(TransportationOrderItem_.class)
						.where(t -> t.order_displayId().eq(o.getDisplayId()))
						.columns("weight", "quantity");

				//BigDecimal totalWeight = 0;
				Result result2 = db.run(selectItem);
				o.setTotalWeight(BigDecimal.valueOf(0));
				result2.list().forEach(i -> {
					Integer quantity;
					BigDecimal weight;
					weight = (BigDecimal) i.get("weight");
				    quantity = (Integer) i.get("quantity");
					BigDecimal weight1 = weight.multiply(BigDecimal.valueOf(quantity));
					o.setTotalWeight(o.getTotalWeight().add(weight1));
				});
			});
		}
	}
}