package com.dapr.customer.config;

import com.dapr.common.DaprConfig;
import com.dapr.customer.model.entity.Customer;
import io.dapr.client.DaprClient;
import java.util.*;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataLoader {

  public static final String CUSTOMERS_KEY = "customers-ids";

  private final Faker faker = new Faker(Locale.ENGLISH);
  private final DaprClient daprClient;

  @Bean
  ApplicationRunner onStart() {

    return args -> {
      log.info("🧬 Generating sample customer data...");

      daprClient
          .getState(DaprConfig.STATE_STORE_NAME, CUSTOMERS_KEY, List.class)
          .flatMap(state -> Mono.justOrEmpty(state.getValue()))
          .defaultIfEmpty(Collections.emptyList())
          .subscribe(
              customersIds -> {
                if (customersIds.isEmpty()) {
                  generateCustomers();
                } else {
                  log.info("🛑 Customer data already exists. Skipping data generation. 🛑");
                }
              });
    };
  }

  private void generateCustomers() {
    var customersIds = new ArrayList<UUID>();
    IntStream.range(1, 10)
        .forEach(
            i -> {
              var firstName = faker.name().firstName();
              var lastName = faker.name().lastName();
              var email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@dapr.com";
              var customer =
                  Customer.builder()
                      .id(java.util.UUID.randomUUID())
                      .firstName(firstName)
                      .lastName(lastName)
                      .email(email)
                      .address(faker.address().fullAddress())
                      .build();

              daprClient
                  .saveState(
                      DaprConfig.STATE_STORE_NAME, String.valueOf(customer.getId()), customer)
                  .doOnSuccess(unused -> customersIds.add(customer.getId()))
                  .block();
            });
    daprClient.saveState(DaprConfig.STATE_STORE_NAME, CUSTOMERS_KEY, customersIds).block();
    log.info("✅ Generated {} sample customers: {}", customersIds, customersIds);
  }
}
