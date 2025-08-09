package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.persistence.entity.Role;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.RoleRepository;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userFound = userRepository.findByEmail(username)
									   .orElseThrow(() -> new NotFoundException("Usuario No Encontrado"));

		Optional<AuthProvider> emailAuthProvider = userFound.getAuthProviders().stream().filter(provider -> provider.getName().equals(AuthProviders.EMAIL)).findFirst();

		Role userRoles = roleRepository.findById(userFound.getRoleId())
									   .orElseThrow(() -> new NotFoundException("Rol No Encontrado"));

		String[] roles = {userRoles.getName().toString()};

		org.springframework.security.core.userdetails.User.UserBuilder userBuilder = org.springframework.security.core.userdetails.User.builder()
																																	   .username(userFound.getEmail())
																																	   .roles(roles)
																																	   .disabled(userFound.getDisabled())
																																	   .accountLocked(userFound.getBlocked());

		if (emailAuthProvider.isPresent()) {
			userBuilder.password(emailAuthProvider.get().getPassword());
		} else {
			userBuilder.password("");
		}

		return userBuilder.build();
	}
}
