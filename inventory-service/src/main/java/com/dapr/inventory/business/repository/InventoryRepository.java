package com.dapr.inventory.business.repository;

import com.dapr.common.DaprConfig;
import com.dapr.inventory.model.entity.Product;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.State;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class InventoryRepository {
  private final DaprClient dapr;

  public Flux<Product> getProducts() {

    return dapr.getBulkState(DaprConfig.STATE_STORE_NAME, List.of(), Product.class)
        .flatMapMany(Flux::fromIterable)
        .map(io.dapr.client.domain.State::getValue);
  }

  public Mono<Product> getProductById(String id) {

    return dapr.getState(DaprConfig.STATE_STORE_NAME, id, Product.class)
        .mapNotNull(State::getValue);
  }

  public Mono<Void> saveProduct(Product product) {
    return dapr.saveState(DaprConfig.STATE_STORE_NAME, String.valueOf(product.getId()), product);
  }

  public Mono<Void> deleteProduct(String id) {
    return dapr.deleteState(DaprConfig.STATE_STORE_NAME, id);
  }
}
