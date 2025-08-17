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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

	@Operation(summary = "Agregar un item al carrito activo del usuario", description = "Agregar un item al carrito activo del usuario, si no existe el carrito se crea uno")
	@ApiResponse(responseCode = "200", description = "Item agregado al carrito exitosamente")
	@PostMapping("/add")
	public ResponseEntity<MsgDataResDto<CartItemResDto>> addProductToCart(@RequestBody CartItemReqDto cartItemReqDto, Principal principal) throws JsonProcessingException {
		//productService.validateStockProducts(List.of(cartItemReqDto));
		System.out.println(principal.getName());

		return new ResponseEntity<>(cartItemService.addToCart(cartItemReqDto, principal.getName()), HttpStatus.OK);
	}

	@Operation(summary = "Obtener carrito activo", description = "Obtener carrito activo del usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Carrito activo obtenido de forma exitosa"),
			@ApiResponse(responseCode = "404", description = "No tiene un carrito activo")
	})
	@GetMapping("/active")
	public ResponseEntity<MsgDataResDto<CartResDto>> getActiveCart(Principal principal) {
		return new ResponseEntity<>(cartService.getCartWithItems(principal.getName()), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar item del carrito", description = "Eliminar el item del carrito activo del usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Item eliminado del carrito"),
			@ApiResponse(responseCode = "404", description = "El item no existe en el carrito")
	})
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<MessageResDto> deleteItemFromCart(@PathVariable String id, Principal principal) {
		return new ResponseEntity<>(cartItemService.deleteItemFromCart(id, principal.getName()), HttpStatus.OK);
	}

	@Operation(summary = "Cambiar el estatus del carrito", description = "Cambiar el estatus del carrito activo del usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Estatus del carrito cambiado"),
			@ApiResponse(responseCode = "404", description = "No tiene un carrito activo")
	})
	@PostMapping("/change-status")
	public ResponseEntity<MessageResDto> changeCartStatus(Principal principal) {
		return new ResponseEntity<>(cartService.changeCartStatus(principal.getName()), HttpStatus.OK);
	}
}
