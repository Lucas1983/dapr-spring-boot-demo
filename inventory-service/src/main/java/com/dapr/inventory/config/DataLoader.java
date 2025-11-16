package com.dapr.inventory.config;

import com.dapr.common.DaprConfig;
import com.dapr.inventory.model.entity.Product;
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

  public static final String PRODUCTS_KEY = "products-ids";

  private final Faker faker = new Faker(Locale.ENGLISH);
  private final DaprClient daprClient;

  @Bean
  ApplicationRunner onStart() {
    return args -> {
      log.info("🚀 Generating sample product data...");

      daprClient
          .getState(DaprConfig.STATE_STORE_NAME, PRODUCTS_KEY, List.class)
          .flatMap(state -> Mono.justOrEmpty(state.getValue()))
          .defaultIfEmpty(Collections.emptyList())
          .subscribe(
              productsIds -> {
                if (productsIds.isEmpty()) {
                  generateProducts();
                } else {
                  log.info("🛑 Product data already exists. Skipping data generation. 🛑");
                }
              });
    };
  }

  private void generateProducts() {
    var productsIds = new ArrayList<UUID>();
    IntStream.range(1, 10)
        .forEach(
            i -> {
              var productName = faker.commerce().productName();
              var product =
                  Product.builder()
                      .id(java.util.UUID.randomUUID())
                      .name(productName)
                      .description(productName)
                      .quantity(100)
                      .build();

              daprClient
                  .saveState(DaprConfig.STATE_STORE_NAME, String.valueOf(product.getId()), product)
                  .doOnSuccess(unused -> productsIds.add(product.getId()))
                  .block();
            });
    daprClient.saveState(DaprConfig.STATE_STORE_NAME, PRODUCTS_KEY, productsIds).block();
    log.info("✅ Generated {} sample products: {}", productsIds, productsIds);
  }
}
