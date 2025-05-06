namespace com.test.transport;

using { managed } from '@sap/cds/common';

entity TransportationOrder : managed {
    key displayId : String(20) not null;
    description : localized String;
    virtual totalWeight : Decimal(10,2) ;
    status : String enum { inPlanning; inExecution; };
    items : Composition of many TransportationOrderItem on items.order = $self;
}

entity TransportationOrderItem : managed {
    key displayId : String(20) not null;
    key order : Association to TransportationOrder;
    quantity : Integer;
    weight : Decimal(10,2);
}

view TransportationOrdersWithTotal as
select from TransportationOrder {
  displayId,
  description,
  status,
  items,
  sum(items.weight * items.quantity) as totalWeight : Decimal(10,2)
}
group by displayId, description, status;