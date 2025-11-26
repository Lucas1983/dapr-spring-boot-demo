package com.dapr.shipping.business.workflow.activity;

import com.dapr.shipping.business.repository.ShippingRepository;
import com.dapr.shipping.model.dictionary.ShipmentStatus;
import com.dapr.shipping.model.entity.Shipment;
import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.WorkflowActivityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliverShipmentActivity implements WorkflowActivity {

  private final ShippingRepository shippingRepository;

  @Override
  public Object run(WorkflowActivityContext ctx) {

    var shipment = ctx.getInput(Shipment.class);
    shipment.setShipmentStatus(ShipmentStatus.DELIVERED);
    shippingRepository.saveShipment(shipment).subscribe();
    log.info("✅ Shipment delivered");
    return shipment;
  }
}
