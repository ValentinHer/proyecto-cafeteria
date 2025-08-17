package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductReqDto {

	@NotNull(message = "La categoría es requerida")
	private String categoryId;

	@NotNull(message = "El nombre es requerido")
	@Size(min = 2, max = 100)
	private String name;

	@NotNull(message = "El stock es requerido")
	@Min(1)
	private Integer stock;

	@NotNull(message = "El precio es requerido")
	@DecimalMin(value = "1.00", message = "El precio mínimo es de 1.00")
	@Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta dos decimales")
	private BigDecimal price;

	private String description;

	private Boolean isActive = true;

	public @NotNull(message = "La categoría es requerida") String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(@NotNull(message = "La categoría es requerida") String categoryId) {
		this.categoryId = categoryId;
	}

	public @NotNull(message = "El nombre es requerido") @Size(min = 2, max = 100) String getName() {
		return name;
	}

	public void setName(@NotNull(message = "El nombre es requerido") @Size(min = 2, max = 100) String name) {
		this.name = name;
	}

	@NotNull(message = "El stock es requerido")
	@Min(1)
	public Integer getStock() {
		return stock;
	}

	public void setStock(@NotNull(message = "El stock es requerido") @Min(1) Integer stock) {
		this.stock = stock;
	}

	public @NotNull(message = "El precio es requerido") BigDecimal getPrice() {
		return price;
	}

	public void setPrice(@NotNull(message = "El precio es requerido") BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}
}
