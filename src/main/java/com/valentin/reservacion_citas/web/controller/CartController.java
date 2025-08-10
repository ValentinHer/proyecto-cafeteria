package com.valentin.reservacion_citas.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.valentin.reservacion_citas.domain.service.CartItemService;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.domain.service.ProductService;
import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.response.CartItemResDto;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
	private final ProductService productService;
	private final CartItemService cartItemService;
	private final CartService cartService;

	public CartController(ProductService productService, CartItemService cartItemService, CartService cartService) {
		this.productService = productService;
		this.cartItemService = cartItemService;
		this.cartService = cartService;
	}

	@PostMapping("/add")
	public ResponseEntity<MsgDataResDto<CartItemResDto>> addProductToCart(@RequestBody CartItemReqDto cartItemReqDto, Principal principal) throws JsonProcessingException {
		//productService.validateStockProducts(List.of(cartItemReqDto));
		System.out.println(principal.getName());

		return new ResponseEntity<>(cartItemService.addToCart(cartItemReqDto, principal.getName()), HttpStatus.OK);
	}

	@GetMapping("/active")
	public ResponseEntity<MsgDataResDto<CartResDto>> getActiveCart(Principal principal) {
		return new ResponseEntity<>(cartService.getCartWithItems(principal.getName()), HttpStatus.OK);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<MessageResDto> deleteItemFromCart(@PathVariable String id, Principal principal) {
		return new ResponseEntity<>(cartItemService.deleteItemFromCart(id, principal.getName()), HttpStatus.OK);
	}

	@PostMapping("/change-status")
	public ResponseEntity<MessageResDto> changeCartStatus(Principal principal) {
		return new ResponseEntity<>(cartService.changeCartStatus(principal.getName()), HttpStatus.OK);
	}
}
