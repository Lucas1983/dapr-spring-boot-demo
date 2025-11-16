package com.dapr.customer.controller;

import com.dapr.customer.business.service.CustomerService;
import com.dapr.customer.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping("/")
  public Flux<Customer> getCustomers() {
    return customerService.getCustomer();
  }

  @GetMapping("/{id}")
  public Mono<Customer> getCustomerById(@PathVariable String id) {
    return customerService.getCustomerById(id);
  }

  @PostMapping("/")
  public Mono<Void> createCustomer(Customer product) {
    return customerService.createCustomer(product);
  }

  @PutMapping("/")
  public Mono<Void> updateCustomer(Customer product) {
    return customerService.updateCustomer(product);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteCustomerById(@PathVariable String id) {
    return customerService.deleteCustomerById(id);
  }
}
