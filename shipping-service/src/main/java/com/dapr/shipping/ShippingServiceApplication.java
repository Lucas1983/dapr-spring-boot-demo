package com.dapr.shipping;

import io.dapr.spring.workflows.config.EnableDaprWorkflows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDaprWorkflows
public class ShippingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShippingServiceApplication.class, args);
  }
}
