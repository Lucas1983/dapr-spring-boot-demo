package com.dapr.customer.business.service;

import com.dapr.customer.business.repository.CustomerRepository;
import com.dapr.customer.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository repository;

  public Flux<Customer> getCustomer() {
    return repository.getCustomers();
  }

  public Mono<Customer> getCustomerById(String id) {
    return repository.getCustomerById(id);
  }

  public Mono<Void> createCustomer(Customer Customer) {
    return repository.saveCustomer(Customer);
  }

  public Mono<Void> deleteCustomerById(String id) {
    return repository.deleteCustomer(id);
  }

  public Mono<Void> updateCustomer(Customer Customer) {
    return repository.saveCustomer(Customer);
  }
}
