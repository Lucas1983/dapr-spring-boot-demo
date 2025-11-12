package com.dapr.common.order.event;

import com.dapr.common.BaseEvent;
import com.dapr.common.EventType;
import java.util.Map;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent extends BaseEvent {

  private UUID orderId;
  private Map<UUID, Integer> products;
  @Builder.Default private EventType type = EventType.ORDER_CREATED;
}
