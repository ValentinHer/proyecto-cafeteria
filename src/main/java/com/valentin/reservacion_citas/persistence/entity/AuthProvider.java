package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "proveedores_auth")
public class AuthProvider {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_proveedor")
	private String id;

	@Column(name = "id_user")
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "nombre")
	private AuthProviders name;

	@Column(name = "proveedor_id", nullable = true)
	private String providerId;

	@Column(name = "correo_electronico", columnDefinition = "VARCHAR(100)", nullable = false)
	private String email;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String password;

	@Column(name = "reset_token", columnDefinition = "TEXT", nullable = true)
	private String resetToken;

	@Column(name = "activo", columnDefinition = "BOOLEAN", nullable = false)
	private Boolean isActive = true;

	@ManyToOne
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
	private User user;

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime updatedAt;

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

	public AuthProviders getName() {
		return name;
	}

	public void setName(AuthProviders name) {
		this.name = name;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
