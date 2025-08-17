package com.valentin.reservacion_citas.domain.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentin.reservacion_citas.domain.mapper.ProductMapper;
import com.valentin.reservacion_citas.domain.service.CloudStorageService;
import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.persistence.entity.Product;
import com.valentin.reservacion_citas.persistence.repository.ProductRepository;
import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.CategoryResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import com.valentin.reservacion_citas.web.exception.ConflictException;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final CategoryServiceImpl categoryService;
	private final CloudStorageService cloudStorageService;
	private final ProductMapper productMapper;
	private final ObjectMapper objectMapper;
	private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	public ProductServiceImpl(ProductRepository productRepository, CategoryServiceImpl categoryService, CloudStorageService cloudStorageService, ProductMapper productMapper, ObjectMapper objectMapper) {
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.cloudStorageService = cloudStorageService;
		this.productMapper = productMapper;
		this.objectMapper = objectMapper;
	}

	@Override
	public MessageResDto create(ProductReqDto productReqDto, MultipartFile file) throws IOException {
		categoryService.getById(productReqDto.getCategoryId());

		String filename = String.format("%s-%s", UUID.randomUUID().toString(), file.getOriginalFilename());
		cloudStorageService.uploadImage(file.getBytes(), filename);

		Product product = productMapper.toEntity(productReqDto);
		product.setImage(filename);

		productRepository.save(product);

		return new MessageResDto("Producto guardado exitosamente", HttpStatus.CREATED.value());
	}

	@Override
	@Transactional
	public ProductResDto getOneById(String id, Boolean withCategory) {
		Product productFound = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

		return productMapper.toResponse(productFound, withCategory, false);
	}

	@Override
	@Transactional
	public List<ProductResDto> getAllActive() {
		return productMapper.toResponseList(productRepository.getByIsActive(true));
	}

	@Override
	public MessageResDto updateById(ProductReqDto productReqDto, MultipartFile file, String id) throws IOException {
		if (productReqDto.getCategoryId() != null) {
			categoryService.getById(productReqDto.getCategoryId());
		}

		Product productFound = getById(id);

		productMapper.toUpdate(productFound, productReqDto);

		if (file != null && !file.isEmpty()) {
			String newFilename = String.format("%s-%s", UUID.randomUUID().toString(), file.getOriginalFilename());
			cloudStorageService.deleteImage(productFound.getImage());
			cloudStorageService.uploadImage(file.getBytes(), newFilename);

			productFound.setImage(newFilename);
		}

		productRepository.save(productFound);

		return new MessageResDto("Producto actualizado exitosamente", HttpStatus.OK.value());
	}

	@Override
	public MessageResDto deleteById(String id) {
		Product productFound = getById(id);

		cloudStorageService.deleteImage(productFound.getImage());
		productRepository.deleteById(id);

		return new MessageResDto("Producto eliminado exitosamente", HttpStatus.OK.value());
	}

	@Override
	public void validateStockProducts(List<CartItemReqDto> products) throws JsonProcessingException {
		List<String> errorResDto = products.stream().map(product -> {
			Product productFound = getById(product.getProductId());

			if (product.getQuantity() > productFound.getStock()) {
				return String.format("producto %s tiene solo %s items disponibles", productFound.getName(), productFound.getStock());
			}

			return null;
		}).filter(Objects::nonNull).toList();

		String errorMsg = objectMapper.writeValueAsString(errorResDto);

		if (!errorResDto.isEmpty()) throw new ConflictException(errorMsg);
	}

	public Product getById(String id) {
		Optional<Product> productFound = productRepository.findById(id);

		if (productFound.isEmpty()) throw new NotFoundException("Producto no encontrado");

		return productFound.get();
	}

	@PostConstruct
	@Transactional
	private void addDefaultProducts() throws IOException {
		if (productRepository.count() > 0) {
			logger.info("Productos existentes");
			return;
		}

		List<Product> products = productList();

		for (Product product : products) {
			CategoryResDto categoryResDto = categoryService.getByName(product.getCategoryId());

			product.setCategoryId(categoryResDto.getId());

			String name = product.getName() + ".jpg";
			Resource resource = new ClassPathResource("/static/img/" + name);

			if (!resource.exists()) {
				throw new FileNotFoundException("Archivo " + name + " no encontrado en recursos estáticos.");
			}

			byte[] imageContent = resource.getContentAsByteArray();
			String filename = String.format("%s-%s", UUID.randomUUID().toString(), name);

			product.setImage(filename);

			cloudStorageService.uploadImage(imageContent, filename);

			logger.info("Archivo {} subido exitosamente desde recursos estáticos", filename);
		}

		products.forEach(product -> {
			productRepository.save(product);
			logger.info("Producto {} guardado exitosamente", product.getName());
		});
	}

	private List<Product> productList() {
		Product product1 = new Product();
		product1.setName("Cafe Helado");
		product1.setDescription("Café negro refrescante servido con hielo, perfecto para los días calurosos. Preparado con granos selectos y un toque de dulzura natural.");
		product1.setCategoryId("Bebidas Frias");
		product1.setStock(100);
		product1.setPrice(new BigDecimal("45.00"));
		product1.setIsActive(true);

		Product product2 = new Product();
		product2.setName("Limonada");
		product2.setDescription("Limonada natural preparada con limones frescos y un toque de menta. Refrescante y revitalizante, ideal para hidratarse.");
		product2.setCategoryId("Bebidas Frias");
		product2.setStock(100);
		product2.setPrice(new BigDecimal("35.00"));
		product2.setIsActive(true);

		Product product3 = new Product();
		product3.setName("Te Helado");
		product3.setDescription("Té negro infusionado en frío con un sutil toque de limón. Bebida ligera y refrescante con propiedades antioxidantes.");
		product3.setCategoryId("Bebidas Frias");
		product3.setStock(100);
		product3.setPrice(new BigDecimal("38.00"));
		product3.setIsActive(true);

		Product product4 = new Product();
		product4.setName("Latte Cremoso");
		product4.setDescription("Café espresso con leche vaporizada y una suave capa de espuma. Perfecto balance entre el sabor del café y la cremosidad de la leche.");
		product4.setCategoryId("Bebidas Calientes");
		product4.setStock(100);
		product4.setPrice(new BigDecimal("75.00"));
		product4.setIsActive(true);

		Product product5 = new Product();
		product5.setName("Cafe");
		product5.setDescription("Café negro tradicional preparado con granos arábica selectos. Aroma intenso y sabor equilibrado para los amantes del café puro.");
		product5.setCategoryId("Bebidas Calientes");
		product5.setStock(100);
		product5.setPrice(new BigDecimal("59.00"));
		product5.setIsActive(true);

		Product product6 = new Product();
		product6.setName("Cafe Especial");
		product6.setDescription("Nuestra mezcla especial de granos tostados a la perfección. Un café con cuerpo y aroma excepcionales, servido a la temperatura ideal.");
		product6.setCategoryId("Bebidas Calientes");
		product6.setStock(100);
		product6.setPrice(new BigDecimal("62.00"));
		product6.setIsActive(true);

		Product product7 = new Product();
		product7.setName("Helado");
		product7.setDescription("Helado artesanal con sabores naturales. Cremoso y suave, perfecto para endulzar cualquier momento del día.");
		product7.setCategoryId("Postres Frios");
		product7.setStock(100);
		product7.setPrice(new BigDecimal("40.00"));
		product7.setIsActive(true);

		Product product8 = new Product();
		product8.setName("Pay de Fresa");
		product8.setDescription("Pay de fresa casero con base de galleta y crema de fresa natural. El equilibrio perfecto entre dulzura y frescura.");
		product8.setCategoryId("Postres Frios");
		product8.setStock(100);
		product8.setPrice(new BigDecimal("45.00"));
		product8.setIsActive(true);

		Product product9 = new Product();
		product9.setName("Pay de Limon");
		product9.setDescription("Pay de limón con base crujiente y relleno cremoso. Sabor cítrico refrescante que combina perfectamente con cualquier bebida.");
		product9.setCategoryId("Postres Frios");
		product9.setStock(100);
		product9.setPrice(new BigDecimal("48.00"));
		product9.setIsActive(true);

		Product product10 = new Product();
		product10.setName("Galletas");
		product10.setDescription("Galletas recién horneadas con chispas de chocolate. Crujientes por fuera y suaves por dentro, acompañadas de un toque de canela.");
		product10.setCategoryId("Postres Calientes");
		product10.setStock(100);
		product10.setPrice(new BigDecimal("30.00"));
		product10.setIsActive(true);

		Product product11 = new Product();
		product11.setName("Hotcakes");
		product11.setDescription("Hotcakes esponjosos servidos con mantequilla y jarabe de maple. Desayuno clásico que nunca falla para comenzar el día.");
		product11.setCategoryId("Postres Calientes");
		product11.setStock(100);
		product11.setPrice(new BigDecimal("55.00"));
		product11.setIsActive(true);

		Product product12 = new Product();
		product12.setName("Waflez");
		product12.setDescription("Waflez belga dorado y crujiente, servido con frutas frescas y crema batida. Textura perfecta y sabor inigualable.");
		product12.setCategoryId("Postres Calientes");
		product12.setStock(100);
		product12.setPrice(new BigDecimal("50.00"));
		product12.setIsActive(true);

		Product product13 = new Product();
		product13.setName("Lasaña");
		product13.setDescription("Lasaña casera con capas de pasta, salsa boloñesa, queso mozzarella y bechamel. Horneada hasta obtener el punto perfecto de gratinado.");
		product13.setCategoryId("Platillos Especialidades");
		product13.setStock(100);
		product13.setPrice(new BigDecimal("90.00"));
		product13.setIsActive(true);

		Product product14 = new Product();
		product14.setName("Pizza Margarita");
		product14.setDescription("Pizza Margarita tradicional con masa artesanal, salsa de tomate, mozzarella fresca y albahaca. Cocida en horno de piedra para el sabor auténtico.");
		product14.setCategoryId("Platillos Especialidades");
		product14.setStock(100);
		product14.setPrice(new BigDecimal("85.00"));
		product14.setIsActive(true);

		Product product15 = new Product();
		product15.setName("Sushi");
		product15.setDescription("Sushi fresco preparado con arroz de primera calidad y pescado de mar. Variedad de sabores y texturas en cada pieza.");
		product15.setCategoryId("Platillos Especialidades");
		product15.setStock(100);
		product15.setPrice(new BigDecimal("110.00"));
		product15.setIsActive(true);

		Product product16 = new Product();
		product16.setName("Chilaquiles Rojos");
		product16.setDescription("Chilaquiles con salsa roja picante, tortillas crujientes, pollo deshebrado, crema y queso fresco. Desayuno mexicano por excelencia.");
		product16.setCategoryId("Platillos Tradicionales");
		product16.setStock(100);
		product16.setPrice(new BigDecimal("70.00"));
		product16.setIsActive(true);

		Product product17 = new Product();
		product17.setName("Chilaquiles Verdes");
		product17.setDescription("Chilaquiles con salsa verde de tomate, cebolla y chile serrano. Acompañados de pollo, crema y queso fresco para un sabor auténtico.");
		product17.setCategoryId("Platillos Tradicionales");
		product17.setStock(100);
		product17.setPrice(new BigDecimal("70.00"));
		product17.setIsActive(true);

		Product product18 = new Product();
		product18.setName("Huevo Mexicano");
		product18.setDescription("Huevos estrellados con salsa de tomate, cebolla y chile. Servidos con frijoles refritos y tortillas calientes. Desayuno nutritivo y sabroso.");
		product18.setCategoryId("Platillos Tradicionales");
		product18.setStock(100);
		product18.setPrice(new BigDecimal("60.00"));
		product18.setIsActive(true);

		return List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9,
					   product10, product11, product12, product13, product14, product15, product16, product17, product18);
	}
}
