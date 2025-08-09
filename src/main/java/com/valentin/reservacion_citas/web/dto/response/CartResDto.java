package com.valentin.reservacion_citas.web.dto.response;

import com.valentin.reservacion_citas.persistence.entity.CartStatus;
import com.valentin.reservacion_citas.persistence.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartResDto {
	private String id;

	private String userId;

	private CartStatus status;

	private BigDecimal totalAmount;

	private List<CartItemResDto> items;

	private LocalDateTime createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CartStatus getStatus() {
		return status;
	}

	public void setStatus(CartStatus status) {
		this.status = status;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<CartItemResDto> getItems() {
		return items;
	}

	public void setItems(List<CartItemResDto> items) {
		this.items = items;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
