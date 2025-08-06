package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "pagos")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_pago")
	private String id;

	@Column(name = "id_metodo_pago")
	private String paymentMethodId;

	@Column(name = "id_usuario")
	private String userId;

	@Column(name = "pago_total", nullable = false)
	private BigDecimal TotalAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "divisa", nullable = false)
	private CurrencyTypes currency;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(name = "id_pago_proveedor")
	private String providerPaymentId;

	@ManyToOne
	@JoinColumn(name = "id_metodo_pago", referencedColumnName = "id_metodo_pago", insertable = false, updatable = false)
	private PaymentMethod paymentMethod;

	@ManyToOne
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
	private User user;

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		TotalAmount = totalAmount;
	}

	public CurrencyTypes getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyTypes currency) {
		this.currency = currency;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getProviderPaymentId() {
		return providerPaymentId;
	}

	public void setProviderPaymentId(String providerPaymentId) {
		this.providerPaymentId = providerPaymentId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
