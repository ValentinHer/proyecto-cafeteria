package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Category;
import com.valentin.reservacion_citas.web.dto.request.CategoryReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryMapper {
	public Category toEntity(CategoryReqDto categoryReqDto) {
		Category category = new Category();
		category.setName(categoryReqDto.getName());
		category.setDecription(categoryReqDto.getDescription());
		category.setIsActive(categoryReqDto.getIsActive());

		return category;
	}

	public CategoryResDto toResponse(Category category) {
		CategoryResDto response = new CategoryResDto();
		response.setId(category.getId());
		response.setName(category.getName());
		response.setDescription(category.getDecription());
		response.setCreatedAt(category.getCreatedAt());

		return response;
	}

	public List<CategoryResDto> toResponseList(List<Category> categories) {
		return categories.stream().map(this::toResponse).toList();
	}

	public void toUpdate(Category category, CategoryReqDto categoryReqDto) {
		Optional.ofNullable(categoryReqDto.getName()).ifPresent(category::setName);
		Optional.ofNullable(categoryReqDto.getDescription()).ifPresent(category::setDecription);
		Optional.ofNullable(categoryReqDto.getIsActive()).ifPresent(category::setIsActive);
	}
}
