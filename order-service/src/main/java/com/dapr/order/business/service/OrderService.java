package com.dapr.order.business.service;

import static com.dapr.common.DaprConfig.*;
import static com.dapr.order.model.dictionary.OrderStatus.NEW;

import com.dapr.common.customer.dto.GetCustomerDto;
import com.dapr.common.order.dto.CreateOrderDto;
import com.dapr.common.order.event.OrderCanceledEvent;
import com.dapr.common.order.event.OrderCompletedEvent;
import com.dapr.common.order.event.OrderCreatedEvent;
import com.dapr.order.business.repository.OrderRepository;
import com.dapr.order.model.dictionary.OrderStatus;
import com.dapr.order.model.entity.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import java.time.Instant;
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
public class OrderService {

  private final OrderRepository orderRepository;

  private final DaprClient dapr;

  public Flux<Order> getOrders() {
    return orderRepository.getOrders();
  }

  public Mono<Order> getOrder(UUID id) {
    return orderRepository.getOrder(id);
  }

  public Mono<Void> createOrder(CreateOrderDto dto) {

    return dapr.invokeMethod(
            CUSTOMER_SERVICE,
            "/customer/" + dto.getCustomerId(),
            null,
            HttpExtension.GET,
            GetCustomerDto.class)
        .doOnError(e -> log.error("🛑 Failed to get customer from remote service", e))
        .then(
            Mono.fromSupplier(
                () ->
                    Order.builder()
                        .orderId(UUID.randomUUID())
                        .customerId(dto.getCustomerId())
                        .products(dto.getProducts() != null ? dto.getProducts() : Map.of())
                        .orderStatus(NEW)
                        .createdAt(Instant.now().toString())
                        .build()))
        .doOnError(e -> log.error("🛑 Failed to build order object", e))
        .flatMap(
            order ->
                orderRepository
                    .saveOrder(order)
                    .doOnSuccess(v -> log.info("✅ Order saved: {}", order.getOrderId()))
                    .then(
                        dapr.publishEvent(
                                PUBSUB_NAME,
                                ORDER_TOPIC,
                                OrderCreatedEvent.builder()
                                    .orderId(order.getOrderId())
                                    .products(order.getProducts())
                                    .build())
                            .doOnSuccess(
                                v -> log.info("📤 Published ORDER CREATED event: {}", order))))
        .doOnError(e -> log.error("🛑 Create order failed", e))
        .then();
  }

  public Mono<Void> updateOrderStatus(UUID id, OrderStatus orderStatus) {

    return orderRepository
        .getOrder(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("🛑 Order not found: " + id)))
        .flatMap(
            order -> {
              order.setOrderStatus(orderStatus);
              return orderRepository
                  .saveOrder(order)
                  .doOnSuccess(v -> log.info("✅ Updated order {}", order))
                  .then(processStatus(order)); // next step depends on status
            })
        .doOnError(e -> log.error("🛑 Error updating order status for {}: {}", id, e.getMessage()))
        .then();
  }

  private Mono<Void> processStatus(Order order) {
    return switch (order.getOrderStatus()) {
      case PROCESSING -> processOrder(order);
      case COMPLETED -> completeOrder(order);
      case CANCELLED -> cancelOrder(order);
      default ->
          Mono.error(
              new IllegalStateException("🛑 Unexpected order status: " + order.getOrderStatus()));
    };
  }

  private Mono<Void> processOrder(Order order) {
    log.info("Order is being processed : {}", order);
    return Mono.empty();
  }

  private Mono<Void> completeOrder(Order order) {
    log.info("Order has been completed : {}", order);
    var event =
        OrderCompletedEvent.builder()
            .orderId(order.getOrderId())
            .customerId(order.getCustomerId())
            .build();
    return dapr.publishEvent(PUBSUB_NAME, ORDER_TOPIC, event)
        .doOnSuccess(unused -> log.info("📤 Published order COMPLETED event : {}", event));
  }

  private Mono<Void> cancelOrder(Order order) {
    log.info("Order has been cancelled : {}", order);
    var event = OrderCanceledEvent.builder().orderId(order.getOrderId()).build();
    return dapr.publishEvent(PUBSUB_NAME, ORDER_TOPIC, event)
        .doOnSuccess(unused -> log.info("📤 Published order CANCELLED event : {}", event));
  }

  public Mono<Void> removeOrder(UUID id) {
    return orderRepository.deleteOrder(id);
  }
}
