package com.valentin.reservacion_citas.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_usuario")
	private String id;

	@Column(name = "nombre", columnDefinition = "VARCHAR(50)", nullable = false)
	private String name;

	@Column(name = "correo_electronico", columnDefinition = "VARCHAR(100)", nullable = false)
	private String email;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Appointment> appointments = new ArrayList<>();

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

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
}
