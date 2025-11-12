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
public class OrderCompletedEvent extends BaseEvent {

  private UUID orderId;
  private UUID customerId;
  @Builder.Default private EventType type = EventType.ORDER_COMPLETED;
}
