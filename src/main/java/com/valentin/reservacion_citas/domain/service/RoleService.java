package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.Role;

public interface RoleService {
	Role findByName(String name);
}
