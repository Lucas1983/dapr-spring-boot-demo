package com.dapr.common.order.event;

import com.dapr.common.BaseEvent;
import com.dapr.common.EventType;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCanceledEvent extends BaseEvent {

  private UUID orderId;
  @Builder.Default private EventType type = EventType.ORDER_CANCELLED;
}
