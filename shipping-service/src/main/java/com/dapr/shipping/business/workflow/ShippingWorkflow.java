package com.dapr.shipping.business.workflow;

import com.dapr.shipping.business.workflow.activity.DeliverShipmentActivity;
import com.dapr.shipping.business.workflow.activity.DispatchShipmentActivity;
import com.dapr.shipping.business.workflow.activity.PrepareShipmentActivity;
import com.dapr.shipping.model.dictionary.ShipmentStatus;
import com.dapr.shipping.model.entity.Shipment;
import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShippingWorkflow implements Workflow {

  @Override
  public WorkflowStub create() {

    return ctx -> {
      var shipment = ctx.getInput(Shipment.class);

      ctx.waitForExternalEvent(ShipmentStatus.PENDING.name()).await();
      ctx.callActivity(PrepareShipmentActivity.class.getName(), shipment).await();

      ctx.waitForExternalEvent(ShipmentStatus.SHIPPED.name()).await();
      ctx.callActivity(DispatchShipmentActivity.class.getName(), shipment).await();

      ctx.waitForExternalEvent(ShipmentStatus.DELIVERED.name()).await();
      ctx.callActivity(DeliverShipmentActivity.class.getName(), shipment).await();

      log.info("🎁 Shipment workflow completed for shipmentId: {}", shipment.getShipmentId());
      ctx.complete(shipment);
    };
  }
}
