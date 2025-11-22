package com.dapr.shipping.business.workflow.activity;

import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.WorkflowActivityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliverShipmentActivity implements WorkflowActivity {
  @Override
  public Object run(WorkflowActivityContext ctx) {
    log.info("✅ Shipment delivered");
    return null;
  }
}
