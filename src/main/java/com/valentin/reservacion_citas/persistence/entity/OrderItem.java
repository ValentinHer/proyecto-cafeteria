package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "items_orden")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "orden_item_id")
	private String id;

	@Column(name = "id_producto")
	private String productId;

	@Column(name = "cantidad", nullable = false)
	private Integer quantity;

	@Column(name = "precio_unitario", nullable = false)
	private BigDecimal unitaryPrice;

	@Column(name = "pago_total", nullable = false)
	private BigDecimal totalAmount;

	@ManyToOne
	@JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "id_orden", referencedColumnName = "id_orden", insertable = false, updatable = false)
	private Order order;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
