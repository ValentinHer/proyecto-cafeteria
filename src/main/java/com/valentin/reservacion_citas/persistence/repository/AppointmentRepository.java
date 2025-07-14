package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.Appointment;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends ListCrudRepository<Appointment, String> {
}
