package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.CategoryService;
import com.valentin.reservacion_citas.web.dto.request.CategoryReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<MessageResDto> create(@RequestBody CategoryReqDto categoryReqDto) {
		return new ResponseEntity<>(categoryService.create(categoryReqDto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<CategoryResDto>> getAllActive() {
		return new ResponseEntity<>(categoryService.getAllActive(), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<MessageResDto> update(@RequestBody CategoryReqDto categoryReqDto, @PathVariable("id") String id) {
		return new ResponseEntity<>(categoryService.updateById(categoryReqDto, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResDto> delete(@PathVariable("id") String id) {
		return new ResponseEntity<>(categoryService.deleteById(id), HttpStatus.OK);
	}
}
