//package com.test.transport.handlers;
//
//import com.sap.cds.ql.Select;
//import com.sap.cds.services.cds.CdsReadEventContext;
//import com.sap.cds.services.cds.CqnService;
//import com.sap.cds.services.handler.EventHandler;
//import com.sap.cds.services.handler.annotations.After;
//import com.sap.cds.services.handler.annotations.ServiceName;
//import com.sap.cds.services.persistence.PersistenceService;
//import org.springframework.stereotype.Component;
//import com.sap.cds.ql.CQL;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Component
//@ServiceName("TransportationService")
//public class TransportationOrderHandler implements EventHandler {
//
//    private final PersistenceService db;
//
//    public TransportationOrderHandler(PersistenceService db) {
//        this.db = db;
//    }
//
//    @After(event = CqnService.EVENT_READ, entity = "TransportationOrderService")
//    public void calculateTotalWeight(List<cds.gen.com.test.transport.transportationservice.TransportationOrderService> orders, CdsReadEventContext context) {
//        for (cds.gen.com.test.transport.transportationservice.TransportationOrderService order : orders) {
//            // Получаем все элементы заказа
//            List<cds.gen.com.test.transport.transportationservice.TransportationOrderItem> items = db.run(Select.from(cds.gen.com.test.transport.transportationservice.TransportationOrderItem_.class)
//                    .where(item -> item.order().eq(order.getDisplayId()))
//                    .listOf(cds.gen.com.test.transport.transportationservice.TransportationOrderItem.class));
//
//            // Рассчитываем общий вес
//            BigDecimal totalWeight = items.stream()
//                    .map(item -> item.getWeight().multiply(BigDecimal.valueOf(item.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            // Устанавливаем виртуальное поле
//            order.setTotalWeight(totalWeight);
//        }
//    }
//}