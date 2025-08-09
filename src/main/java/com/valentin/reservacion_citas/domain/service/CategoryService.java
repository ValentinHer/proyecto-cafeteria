package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.CategoryReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;

import java.util.List;

public interface CategoryService {
	MessageResDto create(CategoryReqDto categoryReqDto);

	List<CategoryResDto> getAllActive();

	CategoryResDto getByName(String name);

	MessageResDto updateById(CategoryReqDto categoryReqDto, String id);

	MessageResDto deleteById(String id);
}
