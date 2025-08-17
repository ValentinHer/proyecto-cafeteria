package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Operation(summary = "Crear producto", description = "Crear nuevo producto")
	@ApiResponse(responseCode = "201", description = "Producto guardado")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MessageResDto> create(@Valid @RequestPart("product") ProductReqDto productReqDto,
												@RequestPart MultipartFile file) throws IOException {
		return new ResponseEntity<>(productService.create(productReqDto, file), HttpStatus.CREATED);
	}

	@Operation(summary = "Obtener productos activos", description = "Obtener todos los productos activos")
	@ApiResponse(responseCode = "200", description = "Productos activos obtenidos")
	@GetMapping
	public ResponseEntity<List<ProductResDto>> getAllActive() {
		return new ResponseEntity<>(productService.getAllActive(), HttpStatus.OK);
	}

	@Operation(summary = "Actualizar producto", description = "Actualizar un producto")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Producto actualizado"),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado")
	})
	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MessageResDto> update(@RequestPart("product") ProductReqDto productReqDto, @RequestPart(required = false) MultipartFile file, @PathVariable String id) throws IOException {
		return new ResponseEntity<>(productService.updateById(productReqDto, file, id), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar producto", description = "Eliminar un producto")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Producto eliminado"),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResDto> delete(@PathVariable String id) {
		return new ResponseEntity<>(productService.deleteById(id), HttpStatus.OK);
	}
}
