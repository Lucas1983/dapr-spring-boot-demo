package com.dapr.inventory.controller;

import com.dapr.inventory.business.service.InventoryService;
import com.dapr.inventory.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping("/")
  public Flux<Product> getProducts() {
    return inventoryService.getProducts();
  }

  @GetMapping("/{id}")
  public Mono<Product> getProductById(@PathVariable String id) {
    return inventoryService.getProductById(id);
  }

  @PostMapping("/")
  public Mono<Void> createProduct(Product product) {
    return inventoryService.createProduct(product);
  }

  @PutMapping("/")
  public Mono<Void> updateProduct(Product product) {
    return inventoryService.updateProduct(product);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteProductById(@PathVariable String id) {
    return inventoryService.deleteProductById(id);
  }
}
