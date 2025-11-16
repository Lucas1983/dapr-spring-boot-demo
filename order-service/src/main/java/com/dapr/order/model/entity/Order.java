package com.dapr.order.model.entity;

import com.dapr.order.model.dictionary.OrderStatus;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  UUID orderId;
  UUID customerId;
  Map<UUID, Integer> products;
  OrderStatus orderStatus;
  String createdAt;
}
