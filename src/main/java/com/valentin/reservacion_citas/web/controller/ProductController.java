package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<MessageResDto> create(@RequestBody ProductReqDto productReqDto) {
		return new ResponseEntity<>(productService.create(productReqDto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ProductResDto>> getAllActive() {
		return new ResponseEntity<>(productService.getAllActive(), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<MessageResDto> update(@RequestBody ProductReqDto productReqDto, @PathVariable("id") String id) {
		return new ResponseEntity<>(productService.updateById(productReqDto, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResDto> delete(@PathVariable String id) {
		return new ResponseEntity<>(productService.deleteById(id), HttpStatus.OK);
	}
}
