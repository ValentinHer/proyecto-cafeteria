package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_cita")
	private String id;

	@Column(name = "hora_inicio", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "hora_fin", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime endTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status;

	@Column(name="created_at", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
