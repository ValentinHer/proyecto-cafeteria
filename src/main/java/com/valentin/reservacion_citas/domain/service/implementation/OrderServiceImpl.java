package com.valentin.reservacion_citas.domain.service.implementation;

import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.domain.service.*;
import com.valentin.reservacion_citas.persistence.entity.*;
import com.valentin.reservacion_citas.persistence.entity.OrderStatus;
import com.valentin.reservacion_citas.persistence.repository.OrderRepository;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final PaypalServerSdkClient paypalServerSdkClient;
	private final ProductService productService;
	private final CartService cartService;
	private final PaymentMethodService paymentMethodService;
	private final PaymentService paymentService;
	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	public OrderServiceImpl(OrderRepository orderRepository, PaypalServerSdkClient paypalServerSdkClient, ProductService productService, CartService cartService, PaymentMethodService paymentMethodService, PaymentService paymentService) {
		this.orderRepository = orderRepository;
		this.paypalServerSdkClient = paypalServerSdkClient;
		this.productService = productService;
		this.cartService = cartService;
		this.paymentMethodService = paymentMethodService;
		this.paymentService = paymentService;
	}

	@Override
	@Transactional
	public com.valentin.reservacion_citas.persistence.entity.Order createOrder(String userId, String cartId, OrderStatus status, BigDecimal totalAmount, String providerPaymentId, Payment payment) {
		com.valentin.reservacion_citas.persistence.entity.Order newOrder = new com.valentin.reservacion_citas.persistence.entity.Order();
		newOrder.setUserId(userId);
		newOrder.setCartId(cartId);
		newOrder.setStatus(status);
		newOrder.setTotalAmount(totalAmount);
		newOrder.setProviderPaymentId(providerPaymentId);
		newOrder.setPayment(payment);

		return orderRepository.save(newOrder);
	}

	@Override
	@Transactional
	public Order createPayPalOrder(String email) {
		try {
			// Obtener actual carrito activo del usuario
			MsgDataResDto<CartResDto> cart = cartService.getCartWithItems(email);

			// Obtener los detalles de cada producto en el carrito actual
			List<ProductResDto> products = cart.getData().getItems().stream().map(item -> productService.getOneById(item.getProductId(), false)).toList();

			// Crear una orden de paypal para returnar el id de la orden, para despues realizar la compra.
			Order orderCreated = createPayPalOrders(cart.getData(), products);

			// Obtener el metodo de pago del usuario en caso de que no exista se crea
			PaymentMethod paymentMethodFound = paymentMethodService.createOrRetrievePaymentMethod(PaymentProviders.PAYPAL, cart.getData().getUserId());

			// Crear el intento de pago para el carrito actual
			Payment paymentFound = paymentService.createPayment(paymentMethodFound.getId(), cart.getData().getUserId(), cart.getData().getId(), cart.getData().getTotalAmount(), orderCreated.getId());

			return orderCreated;
		} catch (Exception e) {
			logger.error("Error al crear la orden", e);
			throw new InternalServerException("Error al crear la orden");
		}
	}

	private Order createPayPalOrders(CartResDto cart, List<ProductResDto> products) throws IOException, ApiException {
		CreateOrderInput createOrderInput = new CreateOrderInput.Builder(
				null,
				new OrderRequest.Builder(
						CheckoutPaymentIntent.CAPTURE,
						Arrays.asList(
								new PurchaseUnitRequest.Builder(
										new AmountWithBreakdown.Builder(
												"MXN",
												cart.getTotalAmount().toString()
										).breakdown(
												new AmountBreakdown.Builder()
														.itemTotal(
																new Money("MXN", cart.getTotalAmount().toString())
														).build()
										).build()
								).items(
										cart.getItems().stream().map(item -> new Item.Builder(
												products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst().get().getName(),
												new Money.Builder("MXN", item.getUnitaryPrice().toString()).build(),
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
	public Order capturePayPalOrder(String orderId) {
		try {
			return capturePayPalOrders(orderId);
		} catch (Exception e) {
			logger.error("Error al capturar la orden", e);
			throw new InternalServerException("Error al capturar la orden");
		}
	}

	private Order capturePayPalOrders(String orderId) throws IOException, ApiException {
		CaptureOrderInput captureOrderInput = new CaptureOrderInput.Builder(
				orderId,
				null
		).build();

		OrdersController ordersController = paypalServerSdkClient.getOrdersController();
		ApiResponse<Order> apiResponse = ordersController.captureOrder(captureOrderInput);
		return apiResponse.getResult();
	}
}
