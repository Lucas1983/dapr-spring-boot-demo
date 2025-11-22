package com.dapr.shipping.business.workflow;

import com.dapr.shipping.business.workflow.activity.DeliverShipmentActivity;
import com.dapr.shipping.business.workflow.activity.DispatchShipmentActivity;
import com.dapr.shipping.business.workflow.activity.PrepareShipmentActivity;
import com.dapr.shipping.model.entity.Shipment;
import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShippingWorkflow implements Workflow {

  @Override
  public WorkflowStub create() {

    return ctx -> {
      Shipment shipment = ctx.getInput(Shipment.class);
      log.info("🚚 Starting shipping workflow for shipmentId: {}", shipment.getShipmentId());

      ctx.callActivity(PrepareShipmentActivity.class.getName()).await();
      ctx.callActivity(DispatchShipmentActivity.class.getName()).await();
      ctx.callActivity(DeliverShipmentActivity.class.getName()).await();

      ctx.complete("🎁 Shipment workflow completed successfully");
    };
  }
}
