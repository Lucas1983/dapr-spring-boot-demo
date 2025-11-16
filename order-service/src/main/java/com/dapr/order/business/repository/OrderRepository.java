package com.dapr.order.business.repository;

import com.dapr.common.DaprConfig;
import com.dapr.order.model.entity.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.State;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final DaprClient dapr;

  public Flux<Order> getOrders() {
    return dapr.getBulkState(DaprConfig.STATE_STORE_NAME, List.of(), Order.class)
        .flatMapMany(Flux::fromIterable)
        .map(State::getValue);
  }

  public Mono<Order> getOrder(UUID id) {
    return dapr.getState(DaprConfig.STATE_STORE_NAME, String.valueOf(id), Order.class)
        .mapNotNull(State::getValue);
  }

  public Mono<Void> saveOrder(Order order) {
    return dapr.saveState(DaprConfig.STATE_STORE_NAME, String.valueOf(order.getOrderId()), order);
  }

  public Mono<Void> deleteOrder(UUID id) {
    return dapr.deleteState(DaprConfig.STATE_STORE_NAME, String.valueOf(id));
  }
}
