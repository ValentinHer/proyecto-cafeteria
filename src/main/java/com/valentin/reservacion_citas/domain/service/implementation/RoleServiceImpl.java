package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.RoleService;
import com.valentin.reservacion_citas.persistence.entity.Role;
import com.valentin.reservacion_citas.persistence.entity.Roles;
import com.valentin.reservacion_citas.persistence.repository.RoleRepository;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;
	private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@PostConstruct
	public void createDefaultRole() {
		if(roleRepository.existsByName(Roles.ADMIN)){
			logger.info("Admin rol ya existente");
		} else {
			Role roleAdmin = new Role();
			roleAdmin.setName(Roles.ADMIN);

			Role roleAdminSaved = roleRepository.save(roleAdmin);
			logger.info("Admin rol creado exitosamente");
		}

		if(roleRepository.existsByName(Roles.USER)) {
			logger.info("User rol ya existente");
		} else {
			Role roleUser = new Role();
			roleUser.setName(Roles.USER);

			Role roleUserSaved = roleRepository.save(roleUser);
			logger.info("User rol creado exitosamente");
		}
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(Roles.valueOf(name.toUpperCase())).orElseThrow(() -> new NotFoundException("Rol no Encontrado"));
	}
}
