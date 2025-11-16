package com.dapr.shipping.business.service;

import com.dapr.shipping.business.repository.ShippingRepository;
import com.dapr.shipping.model.dictionary.ShipmentStatus;
import com.dapr.shipping.model.entity.Shipment;
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
                    "🛑 Error creating shipment for orderId {}: {}", orderId, error.getMessage()));
  }

  public Mono<Void> updateShipmentStatus(UUID id, ShipmentStatus status) {

    return shippingRepository
        .getShipment(id)
        .flatMap(
            shipment -> {
              shipment.setShipmentStatus(status);
              return shippingRepository.saveShipment(shipment);
            })
        .doOnSuccess(unused -> log.info("✅ Updated shipment status : {} to {}", id, status))
        .doOnError(
            error ->
                log.error(
                    "🛑 Error updating shipment status for id {}: {}", id, error.getMessage()));
  }

  public Mono<Void> deleteShipment(UUID id) {
    return shippingRepository
        .deleteShipment(id)
        .doOnSuccess(unused -> log.info("✅Deleted shipment : {}", id))
        .doOnError(
            error -> log.error("🛑 Error deleting shipment for id {}: {}", id, error.getMessage()));
  }
}
