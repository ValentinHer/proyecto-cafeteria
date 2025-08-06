package com.valentin.reservacion_citas.domain.service.implementation;

import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import com.valentin.reservacion_citas.domain.service.OrderService;
import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.web.dto.request.OrderReqDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	private final PaypalServerSdkClient paypalServerSdkClient;
	private final ProductService productService;
	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	public OrderServiceImpl(PaypalServerSdkClient paypalServerSdkClient, ProductService productService) {
		this.paypalServerSdkClient = paypalServerSdkClient;
		this.productService = productService;
	}

	@Override
	public Order createOrder(OrderReqDto orderReqDto) {
		try {
			List<ProductResDto> products = orderReqDto.getItems().stream().map(item -> productService.getOneById(item.getProductId(), false)).toList();

			return createOrders(orderReqDto, products);
		} catch (Exception e) {
			logger.error("Error al crear la orden", e);
			throw new InternalServerException("Error al crear la orden");
		}
	}

	private Order createOrders(OrderReqDto orderReqDto, List<ProductResDto> products) throws IOException, ApiException {
		CreateOrderInput createOrderInput = new CreateOrderInput.Builder(
				null,
				new OrderRequest.Builder(
						CheckoutPaymentIntent.CAPTURE,
						Arrays.asList(
								new PurchaseUnitRequest.Builder(
										new AmountWithBreakdown.Builder(
												"USD",
												orderReqDto.getTotalPurchase().toString()
										).breakdown(
												new AmountBreakdown.Builder()
														.itemTotal(
																new Money("USD", orderReqDto.getTotalPurchase().toString())
														).build()
										).build()
								).items(
										orderReqDto.getItems().stream().map(item -> new Item.Builder(
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
	public Order captureOrder(String orderId) {
		try {
			return captureOrders(orderId);
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
