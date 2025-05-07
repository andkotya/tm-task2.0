namespace com.test.transport;

using com.test.transport from '../db/data-model';
using { cuid } from '@sap/cds/common';

service TransportationService {
    @odata.draft.enabled
    entity TransportationOrderService as projection on transport.TransportationOrder actions{
    action setToExecution();
    action setToPlanning();
    };
}