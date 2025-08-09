package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CartItemReqDto {

	@NotNull(message = "El producto es requerido")
	private String productId;

	@NotNull(message = "La cantidad es requerida")
	@Min(value = 1, message = "La cantidad mínima es de 1")
	private Integer quantity;

	@NotNull(message = "El precio unitario es requerido")
	@DecimalMin(value = "1.00", message = "El precio mínimo es de 1.00")
	@Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta dos decimales")
	private BigDecimal unitaryPrice;

	private BigDecimal totalAmount;

	public @NotNull(message = "El producto es requerido") String getProductId() {
		return productId;
	}

	public void setProductId(@NotNull(message = "El producto es requerido") String productId) {
		this.productId = productId;
	}

	public @NotNull(message = "La cantidad es requerida") @Min(value = 1, message = "La cantidad mínima es de 1") Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(@NotNull(message = "La cantidad es requerida") @Min(value = 1, message = "La cantidad mínima es de 1") Integer quantity) {
		this.quantity = quantity;
	}

	public @NotNull(message = "El precio unitario es requerido") @DecimalMin(value = "1.00", message = "El precio mínimo es de 1.00") @Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta dos decimales") BigDecimal getUnitaryPrice() {
		return unitaryPrice;
	}

	public void setUnitaryPrice(@NotNull(message = "El precio unitario es requerido") @DecimalMin(value = "1.00", message = "El precio mínimo es de 1.00") @Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta dos decimales") BigDecimal unitaryPrice) {
		this.unitaryPrice = unitaryPrice;
	}

	public @NotNull(message = "El total de compra por item es requerido") @Digits(integer = 10, fraction = 2, message = "El total a pagar pot item debe tener hasta dos decimales") BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(@NotNull(message = "El total de compra por item es requerido") @Digits(integer = 10, fraction = 2, message = "El total a pagar pot item debe tener hasta dos decimales") BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}
