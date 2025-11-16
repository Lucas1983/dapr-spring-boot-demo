package com.dapr.order.listener;

import com.dapr.common.DaprConfig;
import com.dapr.common.inventory.event.InventoryReservationFailedEvent;
import com.dapr.common.inventory.event.InventoryReservationSucceededEvent;
import com.dapr.order.business.service.OrderService;
import com.dapr.order.model.dictionary.OrderStatus;
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
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryListener {

  private final OrderService orderService;

  @PostMapping("/default")
  public Mono<Void> onUnknown(CloudEvent<Object> event) {
    log.info("📥 Received unknown event : {}", event.getData());
    return Mono.empty();
  }

  @PostMapping(path = "/reservation/succeeded", consumes = "application/cloudevents+json")
  @Topic(
      name = DaprConfig.INVENTORY_TOPIC,
      pubsubName = DaprConfig.PUBSUB_NAME,
      rule = @Rule(match = "event.data.type == 'INVENTORY_RESERVATION_SUCCEEDED'", priority = 0))
  public Mono<Void> onInventoryReservationSucceed(
      @RequestBody CloudEvent<InventoryReservationSucceededEvent> event) {

    log.info("📥 Received INVENTORY RESERVATION SUCCEED event : {}", event.getData());
    return orderService.updateOrderStatus(event.getData().getOrderId(), OrderStatus.PROCESSING);
  }

  @PostMapping(path = "/reservation/failed", consumes = "application/cloudevents+json")
  @Topic(
      name = DaprConfig.INVENTORY_TOPIC,
      pubsubName = DaprConfig.PUBSUB_NAME,
      rule = @Rule(match = "event.data.type == 'INVENTORY_RESERVATION_FAILED'", priority = 1))
  public Mono<Void> onInventoryReservationFailed(
      @RequestBody CloudEvent<InventoryReservationFailedEvent> event) {

    log.info("📥 Received INVENTORY RESERVATION FAILED event : {}", event.getData());
    // TODO: Implement inventory cancellation logic - release reserved inventory
    return Mono.empty();
  }
}
