package com.test.transport.handlers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.sap.cds.Result;
import com.sap.cds.ql.CQL;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Predicate;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.Modifier;
import com.sap.cds.services.cds.CdsCreateEventContext;
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
import com.sap.cds.ql.Select;

import cds.gen.com.test.transport.transportationservice.TransportationService_;
import cds.gen.com.test.transport.transportationservice.TransportationOrderService_;
import cds.gen.com.test.transport.transportationservice.TransportationService;
import cds.gen.com.test.transport.transportationservice.TransportationOrderService;
import cds.gen.com.test.transport.transportationservice.TransportationOrderItem;
import cds.gen.com.test.transport.TransportationOrder;
import cds.gen.com.test.transport.TransportationOrder_;
import cds.gen.com.test.transport.transportationservice.TransportationOrderService_;
//import cds.gen.com.test.transport.transportationservice.TransportationOrderItem;


@Component
@ServiceName(TransportationService_.CDS_NAME)
class CatalogServiceHandler implements EventHandler {
	private static final String STATUS_PLANNING = "inPlanning";
	private static final String STATUS_EXECUTION = "inExecution";

	private final PersistenceService db;

    public CatalogServiceHandler(PersistenceService db) {
        this.db = db;
    }

//	@Before(event = CqnService.EVENT_READ)
//	public void calculateTotalWeight(List<cds.gen.com.test.transport.TransportationOrder> orders, CdsReadEventContext context) {
//		// Для каждого заказа вычисляем общий вес
//		for (cds.gen.com.test.transport.TransportationOrder order : orders) {
//			String orderId = (String) order.get("displayId");
//
//			// Запрашиваем все элементы заказа
//			Result items = db.run(Select.from("com.test.transport.TransportationOrderItem")
//							.where(i -> i.get("order").eq(orderId))
//							.columns("quantity", "weight"));
//
//			// Вычисляем общий вес: сумма (quantity * weight) для всех элементов
//			BigDecimal totalWeight = items.stream()
//					.map(item -> {
//						Integer quantity = (Integer) item.get("quantity");
//						BigDecimal weight = (BigDecimal) item.get("weight");
//						return weight.multiply(BigDecimal.valueOf(quantity));
//					})
//					.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//			// Устанавливаем вычисленное значение
//			//order.put("totalWeight", totalWeight);
//			order.setTotalWeight(BigDecimal.valueOf(81211.26));
//			System.out.println("TOTALWEEEEIGHT HELLO" + totalWeight);
//		}
//	}

//	@After(event = CqnService.EVENT_READ, entity = "TransportationOrderItem")
//	public void test(Stream<TransportationOrderItem> items) {
//		items.forEach(i -> i.setQuantity(i.getQuantity() + 10));
//	}

//	@On(event = { CqnService.EVENT_CREATE, CqnService.EVENT_READ }, entity = TransportationOrderService_.CDS_NAME)
//	public void beforeCreate(TransportationOrderService order) {
//
//		order.setStatus("inPlanning");
//	}
	@After(event = CqnService.EVENT_READ, entity = TransportationOrderService_.CDS_NAME)
	public void setStartStatus(Stream<TransportationOrderService> orders) {
		if (orders != null){
			orders.forEach(o -> {
				o.setStatus(STATUS_PLANNING);
			});
		}
	}

	@After(event = CqnService.EVENT_READ, entity = TransportationOrderService_.CDS_NAME)//(event = CqnService.EVENT_READ)
	public void calculateTotalWeights(Stream<TransportationOrderService> orders) {
		if (orders != null) {
			orders.forEach(o -> {
//				o.getItems().forEach(i -> {
//					i.
//				});
				o.setTotalWeight(BigDecimal.valueOf(100));
			});
		}
	}
}