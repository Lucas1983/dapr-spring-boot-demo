package com.dapr.shipping.business.repository;

import com.dapr.common.DaprConfig;
import com.dapr.shipping.model.entity.Shipment;
import io.dapr.client.DaprClient;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ShippingRepository {

  private final DaprClient dapr;

  public Flux<Shipment> getShipments() {
    return dapr.getBulkState(DaprConfig.STATE_STORE_NAME, List.of(), Shipment.class)
        .flatMapMany(Flux::fromIterable)
        .map(io.dapr.client.domain.State::getValue);
  }

  public Mono<Shipment> getShipment(UUID id) {
    return dapr.getState(DaprConfig.STATE_STORE_NAME, String.valueOf(id), Shipment.class)
        .mapNotNull(io.dapr.client.domain.State::getValue);
  }

  public Mono<Void> saveShipment(Shipment shipment) {
    return dapr.saveState(
        DaprConfig.STATE_STORE_NAME, String.valueOf(shipment.getShipmentId()), shipment);
  }

  public Mono<Void> deleteShipment(UUID id) {
    return dapr.deleteState(DaprConfig.STATE_STORE_NAME, String.valueOf(id));
  }
}
