package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "metodos_pago")
public class PaymentMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_metodo_pago")
	private String id;

	@Column(name = "id_usuario")
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "proveedor_pago", unique = true)
	private PaymentProviders provider;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
	private PaymentTypes type;

	@Column(name = "proveedor_value")
	private String providerValue;

	@Column(name = "activo")
	private Boolean isActive;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public PaymentProviders getProvider() {
		return provider;
	}

	public void setProvider(PaymentProviders provider) {
		this.provider = provider;
	}

	public PaymentTypes getType() {
		return type;
	}

	public void setType(PaymentTypes type) {
		this.type = type;
	}

	public String getProviderValue() {
		return providerValue;
	}

	public void setProviderValue(String providerValue) {
		this.providerValue = providerValue;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
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
