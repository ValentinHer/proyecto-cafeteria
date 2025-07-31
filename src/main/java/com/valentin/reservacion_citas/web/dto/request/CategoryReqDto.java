package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryReqDto {

	@NotNull(message = "El nombre es requerido")
	@Size(min = 2, max = 50)
	private String name;

	private String description;

	private Boolean isActive = true;

	public @NotNull(message = "El nombre es requerido") @Size(min = 2, max = 50) String getName() {
		return name;
	}

	public void setName(@NotNull(message = "El nombre es requerido") @Size(min = 2, max = 50) String name) {
		this.name = name;
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
