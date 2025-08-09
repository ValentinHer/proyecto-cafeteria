package com.valentin.reservacion_citas.domain.service.implementation;

import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.domain.mapper.OrderMapper;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.domain.service.OrderService;
import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.*;
import com.valentin.reservacion_citas.persistence.repository.CartRepository;
import com.valentin.reservacion_citas.persistence.repository.OrderRepository;
import com.valentin.reservacion_citas.persistence.repository.PaymentRepository;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	private final PaypalServerSdkClient paypalServerSdkClient;
	private final OrderRepository orderRepository;
	private final PaymentRepository paymentRepository;
	private final CartRepository cartRepository;
	private final ProductService productService;
	private final CartService cartService;
	private final UserService userService;
	private final OrderMapper orderMapper;
	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	public OrderServiceImpl(PaypalServerSdkClient paypalServerSdkClient, OrderRepository orderRepository, PaymentRepository paymentRepository, CartRepository cartRepository, ProductService productService, CartService cartService, UserService userService, OrderMapper orderMapper) {
		this.paypalServerSdkClient = paypalServerSdkClient;
		this.orderRepository = orderRepository;
		this.paymentRepository = paymentRepository;
		this.cartRepository = cartRepository;
		this.productService = productService;
		this.cartService = cartService;
		this.userService = userService;
		this.orderMapper = orderMapper;
	}

	@Override
	@Transactional
	public Order createOrder(String email) {
		try {
			Cart cart = cartService.createOrRetrieveActiveCartByUser(email);
			User user = userService.findByEmail(email);

			com.valentin.reservacion_citas.persistence.entity.Order order = orderMapper.toEntityFromCart(cart, user.getId());
			com.valentin.reservacion_citas.persistence.entity.Order orderSaved = orderRepository.save(order);
			logger.info("Orden creadad {}", orderSaved.getId());

			List<ProductResDto> products = orderSaved.getProducts().stream().map(item -> productService.getOneById(item.getProductId(), false)).toList();

			Order orderCreated = createOrders(order, products);

			orderSaved.setProviderOrderId(orderCreated.getId());

			return createOrders(order, products);
		} catch (Exception e) {
			logger.error("Error al crear la orden", e);
			throw new InternalServerException("Error al crear la orden");
		}
	}

	private Order createOrders(com.valentin.reservacion_citas.persistence.entity.Order order, List<ProductResDto> products) throws IOException, ApiException {
		CreateOrderInput createOrderInput = new CreateOrderInput.Builder(
				null,
				new OrderRequest.Builder(
						CheckoutPaymentIntent.CAPTURE,
						Arrays.asList(
								new PurchaseUnitRequest.Builder(
										new AmountWithBreakdown.Builder(
												"USD",
												order.getTotalAmount().toString()
										).breakdown(
												new AmountBreakdown.Builder()
														.itemTotal(
																new Money("USD", order.getTotalAmount().toString())
														).build()
										).build()
								).items(
										order.getProducts().stream().map(item -> new Item.Builder(
												products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst().get().getName(),
												new Money.Builder("USD", item.getUnitaryPrice().toString()).build(),
												item.getQuantity().toString()
										).description(products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst().get().getDescription())
												.sku(String.format("%s", products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst().get().getName().substring(0, 3)))
												.category(ItemCategory.PHYSICAL_GOODS)
												.build()
										).toList()
								).build()
						)
				).build()
		).build();

		OrdersController ordersController = paypalServerSdkClient.getOrdersController();
		ApiResponse<Order> apiResponse = ordersController.createOrder(createOrderInput);

		return apiResponse.getResult();
	}

	@Override
	@Transactional
	public Order captureOrder(String orderId, String email) {
		try {
			User user = userService.findByEmail(email);
			Cart cart = cartService.createOrRetrieveActiveCartByUser(email);
			com.valentin.reservacion_citas.persistence.entity.Order order = orderRepository.findByProviderOrderId(orderId).orElseThrow(() -> new NotFoundException("Orden no Encontrada"));

			Order orderCaptured = captureOrders(orderId);

			cart.setStatus(CartStatus.ORDERED);
			Cart cartUpdated = cartRepository.save(cart);

			Payment payment = new Payment();
			payment.setCurrency(CurrencyTypes.USD);
			payment.setTotalAmount(order.getTotalAmount());
			payment.setStatus(PaymentStatus.PENDING);
			payment.setUserId(user.getId());

			Payment paymentSaved = paymentRepository.save(payment);
			order.setPayment(paymentSaved);

			com.valentin.reservacion_citas.persistence.entity.Order orderUpdated = orderRepository.save(order);

			return orderCaptured;
		} catch (Exception e) {
			logger.error("Error al capturar la orden", e);
			throw new InternalServerException("Error al capturar la orden");
		}
	}

	private Order captureOrders(String orderId) throws IOException, ApiException {
		CaptureOrderInput captureOrderInput = new CaptureOrderInput.Builder(
				orderId,
				null
		).build();

		OrdersController ordersController = paypalServerSdkClient.getOrdersController();
		ApiResponse<Order> apiResponse = ordersController.captureOrder(captureOrderInput);
		return apiResponse.getResult();
	}
}
