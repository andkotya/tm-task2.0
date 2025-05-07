using com.test.transport as transport from '../srv/cat-service';

annotate transport.TransportationOrder with @(
Capabilities: {
      Updatable: true,
      Insertable: true,
      Editable: true,
      Deletable: true
    },
  UI: {
  SelectionFields: [displayId, description],
    LineItem: [
      {
        $Type: 'UI.DataField',
        Value: displayId,
        Label: 'Order ID'
      },
      {
        $Type: 'UI.DataField',
        Value: description,
        Label: 'Description'
      },
      {
        $Type: 'UI.DataField',
        Value: totalWeight,
        Label: 'Total Weight'
      },
      {
      $Type: 'UI.DataField',
      Value: status,
      Label: 'Status'
      }
    ],

    HeaderInfo: {
      Title: {
        $Type: 'UI.DataField',
        Value: displayId,
        Label: 'Order ID'
      },
      Description: {
        $Type: 'UI.DataField',
        Value: description,
        Label: 'Description'
      }
    },

    Facets: [
      {
        $Type: 'UI.ReferenceFacet',
        Label: 'General Information',
        Target: '@UI.FieldGroup#GeneralInfo'
      },
      {
        $Type: 'UI.ReferenceFacet',
        Label: 'Items',
        Target: 'items/@UI.LineItem'
      }
    ],

    FieldGroup#GeneralInfo: {
      $Type: 'UI.FieldGroupType',
      Data: [
        {
          $Type: 'UI.DataField',
          Value: displayId,
          Label: 'Order ID'
        },
        {
          $Type: 'UI.DataField',
          Value: description,
          Label: 'Description'
        },
        {
          $Type: 'UI.DataField',
          Value: totalWeight,
          Label: 'Total Weight'
        },
        {
          $Type: 'UI.DataField',
          Value: status,
          Label: 'Status'
        },
        {
          $Type  : 'UI.DataFieldForAction',
          Action : 'TransportationOrderService.setToExecution',
          Label  : 'Execution',
          ![@UI.Hidden] : editHide
        },
        {
           $Type  : 'UI.DataFieldForAction',
           Action : 'TransportationOrderService.setToPlanning',
           Label  : 'Planning',
           ![@UI.Hidden] : false
         },
         {
          $Type: 'UI.DataFieldForAction',
          Action: 'TransportationService.draftActivate',
          Label: 'Activate Draft',
          ![@UI.Hidden] : editHide
        },
        {
          $Type: 'UI.DataFieldForAction',
          Action: 'TransportationService.draftEdit',
          Label: 'Edit',
          ![@UI.Hidden] : editHide
        }
      ]
    },
    Identification: [
      {Value: displayId},
      {Value: description}
    ]
  },
  Common: {
    DraftRoot: true,
    DraftActivationAction: true
  }
);

annotate transport.TransportationOrderItem with @(
Capabilities: {
      Updatable: true,
      Insertable: true,
      Editable: true,
      Deletable: true
    },
  UI: {
    LineItem: [
      {
        $Type: 'UI.DataField',
        Value: displayId,
        Label: 'ID Item'
      },
      {
        $Type: 'UI.DataField',
        Value: quantity,
        Label: 'Quantity'
      },
      {
        $Type: 'UI.DataField',
        Value: weight,
        Label: 'Weight'
      }
    ]
  },
  Common: {
    DraftNode: true,
    DraftActivationAction: true
  }
);




