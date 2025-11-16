package com.dapr.shipping.listener;

import com.dapr.common.DaprConfig;
import com.dapr.common.order.event.OrderCanceledEvent;
import com.dapr.common.order.event.OrderCompletedEvent;
import com.dapr.common.order.event.OrderCreatedEvent;
import com.dapr.shipping.business.service.ShippingService;
import io.dapr.Rule;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderListener {

  private final ShippingService shippingService;

  @PostMapping("/default")
  public void onUnknown(CloudEvent<Object> event) {
    log.info("Received unknown event : {}", event.getData());
  }

  @PostMapping(path = "/created", consumes = "application/cloudevents+json")
  @Topic(
      name = DaprConfig.ORDER_TOPIC,
      pubsubName = DaprConfig.PUBSUB_NAME,
      rule = @Rule(match = "event.data.type == 'ORDER_CREATED'", priority = 0))
  public Mono<Void> onOrderCreated(@RequestBody CloudEvent<OrderCreatedEvent> event) {

    log.info("📥 Received ORDER CREATED event : {}", event.getData());
    return Mono.empty();
  }

  @PostMapping(path = "/completed", consumes = "application/cloudevents+json")
  @Topic(
      name = DaprConfig.ORDER_TOPIC,
      pubsubName = DaprConfig.PUBSUB_NAME,
      rule = @Rule(match = "event.data.type == 'ORDER_COMPLETED'", priority = 1))
  public Mono<Void> onOrderCompleted(@RequestBody CloudEvent<OrderCompletedEvent> event) {

    log.info("📥 Received ORDER COMPLETED event : {}", event.getData());
    return shippingService.createShipment(event.getData().getOrderId());
  }

  @PostMapping(path = "/cancelled", consumes = "application/cloudevents+json")
  @Topic(
      name = DaprConfig.ORDER_TOPIC,
      pubsubName = DaprConfig.PUBSUB_NAME,
      rule = @Rule(match = "event.data.type == 'ORDER_CANCELLED'", priority = 2))
  public Mono<Void> onOrderCancelled(@RequestBody CloudEvent<OrderCanceledEvent> event) {

    log.info("📥 Received ORDER CANCELLED event {}", event.getData());
    // TODO: Implement shipment cancellation logic - remove shipment if exists
    return Mono.empty();
  }
}
