package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invitados")
public class Guest {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_invitado")
	private String id;

	@Column(name = "nombre", columnDefinition = "VARCHAR(50)", nullable = false)
	private String name;

	@Column(name = "correo_electronico", columnDefinition = "VARCHAR(100)", nullable = false)
	private String email;

	@Column(name = "activo", columnDefinition = "BOOLEAN", nullable = false)
	private Boolean isActive = true;

	@OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Appointment> appointments = new ArrayList<>();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
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
