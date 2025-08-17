package com.valentin.reservacion_citas.domain.mapper;


import com.valentin.reservacion_citas.domain.service.CloudStorageService;
import com.valentin.reservacion_citas.persistence.entity.Product;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {
	private final CategoryMapper categoryMapper;
	private final CloudStorageService cloudStorageService;

	public ProductMapper(CategoryMapper categoryMapper, CloudStorageService cloudStorageService) {
		this.categoryMapper = categoryMapper;
		this.cloudStorageService = cloudStorageService;
	}

	public Product toEntity(ProductReqDto productReqDto) {
		Product product = new Product();
		product.setCategoryId(productReqDto.getCategoryId());
		product.setName(productReqDto.getName());
		product.setDescription(productReqDto.getDescription());
		product.setStock(productReqDto.getStock());
		product.setPrice(productReqDto.getPrice());
		product.setIsActive(productReqDto.getIsActive());

		return product;
	}

	public ProductResDto toResponse(Product product, Boolean withCategory, Boolean withUrlImage) {
		ProductResDto response = new ProductResDto();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setStock(product.getStock());
		response.setPrice(product.getPrice());

		if (withUrlImage) {
			response.setUrlImage(cloudStorageService.getImagePresignedUrl(product.getImage()));
		}

		if (withCategory) {
			response.setCategory(categoryMapper.toResponse(product.getCategory()));
		}

		response.setCreatedAt(product.getCreatedAt());

		return response;
	}

	public List<ProductResDto> toResponseList(List<Product> products) {
		return products.stream().map(product -> toResponse(product, true, true)).toList();
	}

	public void toUpdate(Product product, ProductReqDto productReqDto) {
		Optional.ofNullable(productReqDto.getName()).ifPresent(product::setName);
		Optional.ofNullable(productReqDto.getCategoryId()).ifPresent(product::setCategoryId);
		Optional.ofNullable(productReqDto.getDescription()).ifPresent(product::setDescription);
		Optional.ofNullable(productReqDto.getStock()).ifPresent(product::setStock);
		Optional.ofNullable(productReqDto.getPrice()).ifPresent(product::setPrice);
		Optional.ofNullable(productReqDto.getIsActive()).ifPresent(product::setIsActive);
	}
}
