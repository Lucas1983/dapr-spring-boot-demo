package com.dapr.customer.business.repository;

import com.dapr.common.DaprConfig;
import com.dapr.customer.model.entity.Customer;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.State;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

  private final DaprClient dapr;

  public Flux<Customer> getCustomers() {
    return dapr.getBulkState(DaprConfig.STATE_STORE_NAME, List.of(), Customer.class)
        .flatMapMany(Flux::fromIterable)
        .map(State::getValue);
  }

  public Mono<Customer> getCustomerById(String id) {
    return dapr.getState(DaprConfig.STATE_STORE_NAME, id, Customer.class)
        .mapNotNull(State::getValue);
  }

  public Mono<Void> saveCustomer(Customer customer) {
    return dapr.saveState(DaprConfig.STATE_STORE_NAME, String.valueOf(customer.getId()), customer);
  }

  public Mono<Void> deleteCustomer(String id) {
    return dapr.deleteState(DaprConfig.STATE_STORE_NAME, id);
  }
}
