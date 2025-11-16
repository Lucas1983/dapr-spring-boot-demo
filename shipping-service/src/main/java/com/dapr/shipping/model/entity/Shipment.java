package com.dapr.shipping.model.entity;

import com.dapr.shipping.model.dictionary.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

	private UUID shipmentId;
	private UUID orderId;
	private ShipmentStatus shipmentStatus;
}
