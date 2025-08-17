package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.CategoryService;
import com.valentin.reservacion_citas.web.dto.request.CategoryReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "Crear una nueva categoría", description = "Crear una nueva categoría")
	@ApiResponse(responseCode = "201", description = "Categoría guardada")
	@PostMapping
	public ResponseEntity<MessageResDto> create(@RequestBody CategoryReqDto categoryReqDto) {
		return new ResponseEntity<>(categoryService.create(categoryReqDto), HttpStatus.CREATED);
	}

	@Operation(summary = "Obtener categorías activas", description = "Obtener todas las categorías activas")
	@ApiResponse(responseCode = "200", description = "Categorías activas obtenidas de forma exitosa")
	@GetMapping
	public ResponseEntity<List<CategoryResDto>> getAllActive() {
		return new ResponseEntity<>(categoryService.getAllActive(), HttpStatus.OK);
	}

	@Operation(summary = "Actualizar categoría", description = "Actualizar una categoría")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Categoría actualizado de forma exitosa"),
			@ApiResponse(responseCode = "404", description = "Categoría no encontrada")
	})
	@PatchMapping("/{id}")
	public ResponseEntity<MessageResDto> update(@RequestBody CategoryReqDto categoryReqDto, @PathVariable("id") String id) {
		return new ResponseEntity<>(categoryService.updateById(categoryReqDto, id), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar categoría", description = "Eliminar una categoría")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Categoría eliminada de forma exitosa"),
			@ApiResponse(responseCode = "404", description = "Categoría no exncontrada")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResDto> delete(@PathVariable("id") String id) {
		return new ResponseEntity<>(categoryService.deleteById(id), HttpStatus.OK);
	}
}
