package com.valentin.reservacion_citas.web.dto.response;

import java.math.BigDecimal;

public class OrderItemResDto {
	private String id;

	private String productId;

	private Integer quantity;

	private BigDecimal unitaryPrice;

	private BigDecimal totalAmount;

	private ProductResDto productResDto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitaryPrice() {
		return unitaryPrice;
	}

	public void setUnitaryPrice(BigDecimal unitaryPrice) {
		this.unitaryPrice = unitaryPrice;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ProductResDto getProductResDto() {
		return productResDto;
	}

	public void setProductResDto(ProductResDto productResDto) {
		this.productResDto = productResDto;
	}
}
