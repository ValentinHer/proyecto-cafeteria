package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.CategoryMapper;
import com.valentin.reservacion_citas.domain.service.CategoryService;
import com.valentin.reservacion_citas.persistence.entity.Category;
import com.valentin.reservacion_citas.persistence.repository.CategoryRepository;
import com.valentin.reservacion_citas.web.dto.request.CategoryReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Override
	public MessageResDto create(CategoryReqDto categoryReqDto) {
		Category category = categoryMapper.toEntity(categoryReqDto);
		categoryRepository.save(category);

		return new MessageResDto("Categoría guardada exitosamente", HttpStatus.CREATED.value());
	}

	@Override
	public List<CategoryResDto> getAllActive() {
		return categoryMapper.toResponseList(categoryRepository.findByIsActive(true));
	}

	@Override
	public CategoryResDto getByName(String name) {
		Category categoryFound = categoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));

		return categoryMapper.toResponse(categoryFound);
	}

	@Override
	public MessageResDto updateById(CategoryReqDto categoryReqDto, String id) {
		Category categoryAchieved = getById(id);
		categoryMapper.toUpdate(categoryAchieved, categoryReqDto);

		categoryRepository.save(categoryAchieved);

		return new MessageResDto("Categoría actualizada exitosamente", HttpStatus.OK.value());
	}

	@Override
	public MessageResDto deleteById(String id) {
		Category categoryFound = getById(id);

		categoryRepository.deleteById(id);

		return new MessageResDto("Categoría eliminada exitosamente", HttpStatus.OK.value());
	}

	public Category getById(String id) {
		Optional<Category> categoryFound = categoryRepository.findById(id);

		if (categoryFound.isEmpty()) throw new NotFoundException("Categoría no encontrada");

		return categoryFound.get();
	}

	@PostConstruct
	@Transactional
	private void createDefaultCategories() {
		if (categoryRepository.count() > 0) {
			logger.info("Categorias existentes");
			return;
		}

		List<String> categories = List.of("Bebidas Frias", "Bebidas Calientes", "Postres Frios", "Postres Calientes", "Platillos Especialidades", "Platillos Tradicionales");

		List<Category> categoryList = categories.stream().map(category -> {
			Category categoryEntity = new Category();
			categoryEntity.setName(category);
			categoryEntity.setIsActive(true);

			return categoryEntity;
		}).toList();

		categoryList.forEach(category -> {
			categoryRepository.save(category);
			logger.info("categoria {} guardada", category.getName());
		});
	}
}
