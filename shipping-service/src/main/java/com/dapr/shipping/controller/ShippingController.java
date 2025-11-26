package com.dapr.shipping.controller;

import com.dapr.common.inventory.dto.CreateShipmentDto;
import com.dapr.shipping.business.service.ShippingService;
import com.dapr.shipping.model.entity.Shipment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {
  private final ShippingService shippingService;

  @GetMapping("/shipments")
  public Flux<Shipment> getShipments() {
    return shippingService.getShipments();
  }

  @GetMapping("/shipments/{id}")
  public Mono<Shipment> getShipment(@PathVariable UUID id) {
    return shippingService.getShipment(id);
  }

  @PostMapping("/shipments")
  public Mono<Void> createShipment(CreateShipmentDto dto) {
    return shippingService.createShipment(dto.getOrderId());
  }

  @PutMapping("/{workflowId}/prepare")
  public Mono<Void> prepareShipment(@PathVariable UUID workflowId) {
    return shippingService.prepareShipment(workflowId);
  }

  @PutMapping("/{workflowId}/ship")
  public Mono<Void> shipShipment(@PathVariable UUID workflowId) {
    return shippingService.shipShipment(workflowId);
  }

  @PutMapping("/{workflowId}/deliver")
  public Mono<Void> deliverShipment(@PathVariable UUID workflowId) {
    return shippingService.deliverShipment(workflowId);
  }

  @DeleteMapping("/shipments/{id}")
  public Mono<Void> deleteShipment(@PathVariable UUID id) {
    return shippingService.deleteShipment(id);
  }
}
