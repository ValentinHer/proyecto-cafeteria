package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	public User toEntity(UserReqDto userReqDto) {
		User user = new User();
		user.setName(userReqDto.getName());
		user.setEmail(userReqDto.getEmail());

		return user;
	}
}
