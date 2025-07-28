package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.UserMapper;
import com.valentin.reservacion_citas.domain.service.AuthProviderService;
import com.valentin.reservacion_citas.domain.service.RoleService;
import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.persistence.entity.Role;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final AuthProviderService authProviderService;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserMapper userMapper, AuthProviderService authProviderService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.userMapper = userMapper;
		this.authProviderService = authProviderService;
		this.passwordEncoder = passwordEncoder;
	}


	@Override
	public User createUser(UserReqDto userReqDto) {
		User newUser = userMapper.toEntity(userReqDto);
		Role role = roleService.findByName("USER");

		newUser.setRoleId(role.getId());
		User userSaved = userRepository.save(newUser);
		logger.info("Usuario guardado de forma exitosa: id={}, nombre={}, email={}", userSaved.getId(), userSaved.getName(), userSaved.getEmail());

		return userSaved;
	}

	@Transactional
	@Override
	public MessageResDto createUserWithEmailPassword(UserReqDto userReqDto, AuthProviderReqDto authProviderReqDto) {
		User userCreated = createUser(userReqDto);
		authProviderReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
		AuthProvider authProviderCreated = authProviderService.create(authProviderReqDto, userCreated.getId());

		return new MessageResDto("Usuario Creado Exitosamente", HttpStatus.CREATED.value());
	}

	@Transactional
	@Override
	public MessageResDto createUserWithGoogle(UserReqDto userReqDto, AuthProviderReqDto authProviderReqDto) {
		User userCreated = createUser(userReqDto);
		AuthProvider authProviderCreated = authProviderService.create(authProviderReqDto, userCreated.getId());

		return new MessageResDto("Usuario Creado Exitosamente", HttpStatus.CREATED.value());
	}
}
