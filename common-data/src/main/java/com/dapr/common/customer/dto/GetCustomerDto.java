package com.dapr.common.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerDto {

	private UUID id;
	private String firstName;
	private String lastName;
	private String address;
	private String email;
}
