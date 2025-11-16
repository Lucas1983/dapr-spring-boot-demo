package com.dapr.customer.model.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private UUID id;
  private String firstName;
  private String lastName;
  private String address;
  private String email;
}
