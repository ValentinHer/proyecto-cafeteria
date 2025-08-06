package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class OrderReqDto {
	@NotNull(message = "La lista de items es requerido")
	private List<OrderItemReqDto> items;

	@NotNull(message = "El total de la compra es requerido")
	@Digits(integer = 10, fraction = 2, message = "El total de la compra debe tener hasta dos decimales")
	private BigDecimal totalPurchase;

	public @NotNull(message = "La lista de items es requerido") List<OrderItemReqDto> getItems() {
		return items;
	}

	public void setItems(@NotNull(message = "La lista de items es requerido") List<OrderItemReqDto> items) {
		this.items = items;
	}

	public @NotNull(message = "El total de la compra es requerido") @Digits(integer = 10, fraction = 2, message = "El total de la compra debe tener hasta dos decimales") BigDecimal getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(@NotNull(message = "El total de la compra es requerido") @Digits(integer = 10, fraction = 2, message = "El total de la compra debe tener hasta dos decimales") BigDecimal totalPurchase) {
		this.totalPurchase = totalPurchase;
	}
}
