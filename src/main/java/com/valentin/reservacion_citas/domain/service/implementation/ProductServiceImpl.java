package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.ProductMapper;
import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.persistence.entity.Product;
import com.valentin.reservacion_citas.persistence.repository.ProductRepository;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final CategoryServiceImpl categoryService;
	private final ProductMapper productMapper;

	public ProductServiceImpl(ProductRepository productRepository, CategoryServiceImpl categoryService, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.productMapper = productMapper;
	}

	@Override
	public MessageResDto create(ProductReqDto productReqDto) {
		categoryService.getById(productReqDto.getCategoryId());

		Product product = productMapper.toEntity(productReqDto);
		productRepository.save(product);

		return new MessageResDto("Producto guardado exitosamente", HttpStatus.OK.value());
	}

	@Override
	public List<ProductResDto> getAllActive() {
		return productMapper.toResponseList(productRepository.getByIsActive(true));
	}

	@Override
	public MessageResDto updateById(ProductReqDto productReqDto, String id) {
		if (productReqDto.getCategoryId() != null) {
			categoryService.getById(productReqDto.getCategoryId());
		}

		Product productFound = getById(id);

		productMapper.toUpdate(productFound, productReqDto);
		productRepository.save(productFound);

		return new MessageResDto("Producto actualizado exitosamente", HttpStatus.OK.value());
	}

	@Override
	public MessageResDto deleteById(String id) {
		Product productFound = getById(id);

		productRepository.deleteById(id);

		return new MessageResDto("Producto eliminado exitosamente", HttpStatus.OK.value());
	}

	public Product getById(String id) {
		Optional<Product> productFound = productRepository.findById(id);

		if (productFound.isEmpty()) throw new NotFoundException("Producto no encontrado");

		return productFound.get();
	}
}
