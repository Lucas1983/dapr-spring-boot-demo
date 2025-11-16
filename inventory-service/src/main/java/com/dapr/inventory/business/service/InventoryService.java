package com.dapr.inventory.business.service;

import static com.dapr.common.DaprConfig.INVENTORY_TOPIC;
import static com.dapr.common.DaprConfig.PUBSUB_NAME;

import com.dapr.common.inventory.event.InventoryReservationFailedEvent;
import com.dapr.common.inventory.event.InventoryReservationSucceededEvent;
import com.dapr.inventory.business.repository.InventoryRepository;
import com.dapr.inventory.model.entity.Product;
import io.dapr.client.DaprClient;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository repository;

  private final DaprClient dapr;

  public Flux<Product> getProducts() {
    return repository.getProducts();
  }

  public Mono<Product> getProductById(String id) {
    return repository.getProductById(id);
  }

  public Mono<Void> createProduct(Product product) {
    return repository.saveProduct(product);
  }

  public Mono<Void> deleteProductById(String id) {
    return repository.deleteProduct(id);
  }

  public Mono<Void> updateProduct(Product product) {
    return repository.saveProduct(product);
  }

  public Mono<Boolean> reserveInventory(UUID orderId, Map<UUID, Integer> products) {

    log.info("Reserving inventory for order {} with products {}", orderId, products);
    // TODO: Implement inventory reservation logic - check availability + reserve inventory
    if (orderId == null || products == null || products.isEmpty()) {
      var event = InventoryReservationFailedEvent.builder().orderId(orderId).build();
      return dapr.publishEvent(PUBSUB_NAME, INVENTORY_TOPIC, event)
          .doOnSuccess(v -> log.info("📤Published INVENTORY RESERVATION FAILED event : {}", event))
          .then(Mono.fromCallable(() -> false));
    }
    var event = InventoryReservationSucceededEvent.builder().orderId(orderId).build();
    return dapr.publishEvent(PUBSUB_NAME, INVENTORY_TOPIC, event)
        .doOnSuccess(v -> log.info("📤Published INVENTORY SUCCEED event : {}", event))
        .then(Mono.fromCallable(() -> true));
  }

  public Mono<Void> confirmInventory(UUID uuid) {
    log.info("Confirmed inventory for order {}", uuid);
    // TODO: Implement inventory confirmation logic - deduct reserved inventory + handle errors
    return Mono.empty();
  }
}
