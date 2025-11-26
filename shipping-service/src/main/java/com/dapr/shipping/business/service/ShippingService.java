package com.dapr.shipping.business.service;

import static com.dapr.shipping.model.dictionary.ShipmentStatus.*;

import com.dapr.shipping.business.repository.ShippingRepository;
import com.dapr.shipping.business.workflow.ShippingWorkflow;
import com.dapr.shipping.model.dictionary.ShipmentStatus;
import com.dapr.shipping.model.entity.Shipment;
import io.dapr.workflows.client.DaprWorkflowClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingService {
  private final ShippingRepository shippingRepository;
  private final DaprWorkflowClient daprWfClient;

  public Flux<Shipment> getShipments() {
    return shippingRepository.getShipments();
  }

  public Mono<Shipment> getShipment(UUID id) {
    return shippingRepository.getShipment(id);
  }

  public Mono<Void> createShipment(UUID orderId) {

    Shipment shipment =
        Shipment.builder()
            .shipmentId(UUID.randomUUID())
            .orderId(orderId)
            .shipmentStatus(ShipmentStatus.NEW)
            .build();

    return shippingRepository
        .saveShipment(shipment)
        .doOnSuccess(unused -> log.info("✅ Created shipment : {}", shipment))
        .doOnError(
            error ->
                log.error(
                    "🛑 Error creating shipment for orderId {}: {}", orderId, error.getMessage()))
        .map(unused -> daprWfClient.scheduleNewWorkflow(ShippingWorkflow.class, shipment))
        .doOnSuccess(
            workflowId -> log.info("✅ Scheduled shipping workflow with id: {}", workflowId))
        .doOnError(
            throwable ->
                log.error("🛑 Error scheduling shipping workflow: {}", throwable.getMessage()))
        .then();
  }

  public Mono<Void> updateShipmentStatus(UUID workflowId, ShipmentStatus status) {

    switch (status) {
      case PENDING -> daprWfClient.raiseEvent(workflowId.toString(), PENDING.name(), null);
      case SHIPPED -> daprWfClient.raiseEvent(workflowId.toString(), SHIPPED.name(), null);
      case DELIVERED -> daprWfClient.raiseEvent(workflowId.toString(), DELIVERED.name(), null);
      default -> Mono.error(new IllegalArgumentException("Unsupported shipment status: " + status));
    }
    return Mono.empty();
  }

  public Mono<Void> deleteShipment(UUID id) {
    return shippingRepository
        .deleteShipment(id)
        .doOnSuccess(unused -> log.info("✅Deleted shipment : {}", id))
        .doOnError(
            error -> log.error("🛑 Error deleting shipment for id {}: {}", id, error.getMessage()));
  }
}
