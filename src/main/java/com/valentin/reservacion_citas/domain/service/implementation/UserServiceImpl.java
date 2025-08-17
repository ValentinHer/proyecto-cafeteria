package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.UserMapper;
import com.valentin.reservacion_citas.domain.service.AuthProviderService;
import com.valentin.reservacion_citas.domain.service.NotificationService;
import com.valentin.reservacion_citas.domain.service.RoleService;
import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.persistence.entity.Role;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.AuthProviderRepository;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserChangePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.exception.ConflictException;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final AuthProviderRepository authProviderRepository;
	private final RoleService roleService;
	private final AuthProviderService authProviderService;
	private final NotificationService notificationService;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepository userRepository, AuthProviderRepository authProviderRepository, RoleService roleService, NotificationService notificationService, UserMapper userMapper, AuthProviderService authProviderService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authProviderRepository = authProviderRepository;
		this.roleService = roleService;
		this.notificationService = notificationService;
		this.userMapper = userMapper;
		this.authProviderService = authProviderService;
		this.passwordEncoder = passwordEncoder;
	}

	//private void createDefaultAdminUser()

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
		Optional<User> userFound = userRepository.findByEmail(userReqDto.getEmail());

		if (userFound.isPresent()) {
			User user = userFound.get();
			List<AuthProvider> authProviders = user.getAuthProviders();

			if (authProviders.stream().anyMatch(authProvider -> authProvider.getName().equals(AuthProviders.GOOGLE))) {
				throw new ConflictException("Cuenta existente con este correo. Inicie sesión con google y habilite el método de ingreso por Email/Contraseña");
			} else if (authProviders.stream().anyMatch(authProvider -> authProvider.getName().equals(AuthProviders.EMAIL))) {
				throw new ConflictException("Cuenta existente con este correo. Inicie sesión con su Email/Contraseña o reestablezca su contraseña");
			}
		}

		User userCreated = createUser(userReqDto);
		authProviderReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
		AuthProvider authProviderCreated = authProviderService.create(authProviderReqDto, userCreated.getId());

		return new MessageResDto("Usuario Creado Exitosamente", HttpStatus.CREATED.value());
	}

	@Transactional
	@Override
	public void createUserWithGoogle(UserReqDto userReqDto, AuthProviderReqDto authProviderReqDto) {
		Optional<User> userFound = userRepository.findByEmail(userReqDto.getEmail());

		if (userFound.isPresent()) {
			User user = userFound.get();
			List<AuthProvider> authProviders = user.getAuthProviders();

			if (authProviders.stream().anyMatch(authProvider -> authProvider.getName().equals(AuthProviders.EMAIL))) {
				throw new ConflictException("Cuenta existente con este correo. Inicie sesión con Email/Contraseña y habilite el método de ingreso por Google");
			}

			return;
		}

		User userCreated = createUser(userReqDto);
		AuthProvider authProviderCreated = authProviderService.create(authProviderReqDto, userCreated.getId());
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuario No Encontrado"));
	}

	@Override
	@Transactional
	public MessageResDto restoreUserPassword(String email) {
		User user = findByEmail(email);

		List<AuthProvider> authProviders = user.getAuthProviders();

		if (authProviders.stream().noneMatch(authProvider -> authProvider.getName().equals(AuthProviders.EMAIL))) {
			throw new ConflictException("Su cuenta no cuenta con el método de auténticación por Email/Password");
		}

		notificationService.sendEmailToRestorePassword(user.getName(), user.getEmail());

		return new MessageResDto("Email enviado exitosamente para restaurar su contraseña", HttpStatus.OK.value());
	}

	@Override
	@Transactional
	public MessageResDto changeUserPassword(UserChangePasswordReqDto userChangePasswordReqDto) {
		User user = findByEmail(userChangePasswordReqDto.getEmail());

		List<AuthProvider> authProviders = user.getAuthProviders();

		if (authProviders.stream().noneMatch(authProvider -> authProvider.getName().equals(AuthProviders.EMAIL))) {
			throw new ConflictException("Su cuenta no cuenta con el método de auténticación por Email/Password");
		}

		AuthProvider authProvider = authProviders.stream().filter(authProvide -> authProvide.getName().equals(AuthProviders.EMAIL)).findFirst().get();

		authProvider.setPassword(passwordEncoder.encode(userChangePasswordReqDto.getPassword()));
		AuthProvider authProviderUpdated = authProviderRepository.save(authProvider);

		return new MessageResDto("Contraseña cambiada de forma exitosa", HttpStatus.OK.value());
	}
}
