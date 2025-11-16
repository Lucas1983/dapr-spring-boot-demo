package com.dapr.order.controller;

import com.dapr.common.order.dto.CreateOrderDto;
import com.dapr.order.business.service.OrderService;
import com.dapr.order.model.dictionary.OrderStatus;
import com.dapr.order.model.entity.Order;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public Flux<Order> getOrders() {
    return orderService.getOrders();
  }

  @GetMapping("/{id}")
  public Mono<Order> getOrder(@PathVariable("id") UUID id) {
    return orderService.getOrder(id);
  }

  @PostMapping
  public Mono<Void> createOrder(@RequestBody CreateOrderDto dto) {
    return orderService.createOrder(dto);
  }

  @PutMapping("/{id}/status")
  public Mono<Void> updateOrderStatus(
      @PathVariable("id") UUID id, @RequestParam("status") OrderStatus status) {
    return orderService.updateOrderStatus(id, status);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> removeOrder(@PathVariable("id") UUID id) {
    return orderService.removeOrder(id);
  }
}
