package com.dapr.common.inventory.event;

import com.dapr.common.BaseEvent;
import com.dapr.common.EventType;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservationSucceededEvent extends BaseEvent {

  private UUID orderId;
  @Builder.Default private EventType type = EventType.INVENTORY_RESERVATION_SUCCEEDED;
}
