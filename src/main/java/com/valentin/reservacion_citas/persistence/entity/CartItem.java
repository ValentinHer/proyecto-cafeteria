package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "items_carrito")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "carrito_item_id")
	private String id;

	@Column(name = "id_producto")
	private String productId;

	@Column(name = "id_carrito")
	private String cartId;

	@Column(name = "cantidad", nullable = false)
	private Integer quantity;

	@Column(name = "precio_unitario", nullable = false)
	private BigDecimal unitaryPrice;

	@Column(name = "pago_total", nullable = false)
	private BigDecimal totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_producto", referencedColumnName = "id_producto", insertable = false, updatable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "id_carrito", referencedColumnName = "id_carrito", insertable = false, updatable = false)
	private Cart cart;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
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

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
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

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
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
