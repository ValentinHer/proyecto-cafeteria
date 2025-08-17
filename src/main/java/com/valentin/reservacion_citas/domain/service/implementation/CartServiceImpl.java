package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.CartMapper;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.persistence.entity.CartStatus;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.CartRepository;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgStatus;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final UserService userService;
	private final CartMapper cartMapper;
	private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	public CartServiceImpl(CartRepository cartRepository, UserService userService, CartMapper cartMapper) {
		this.cartRepository = cartRepository;
		this.userService = userService;
		this.cartMapper = cartMapper;
	}

	@Override
	public Cart createOrGetActiveCartByUser(String email) {
		User user = userService.findByEmail(email);

		Optional<Cart> cart = cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE);

		if (cart.isEmpty()) {
			Cart newCart = new Cart();
			newCart.setUserId(user.getId());
			newCart.setStatus(CartStatus.ACTIVE);

			return cartRepository.save(newCart);
		}

		return cart.get();
	}

	@Override
	public Cart getCartById(String cartId) {
		Cart cartFound = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Carrito no encontrado"));
		return cartFound;
	}

	@Override
	@Transactional
	public MsgDataResDto<CartResDto> getCartWithItems(String email) {
		Cart cartFound = getActiveCart(email);

		CartResDto cartResDto = cartMapper.toResponse(cartFound, false);

		MsgDataResDto<CartResDto> response = new MsgDataResDto<>();
		response.setStatus(MsgStatus.SUCCESS);
		response.setMessage("Carrito Activo Recuperado Exitosamente");
		response.setData(cartResDto);

		return response;
	}

	@Override
	@Transactional
	public MessageResDto changeCartStatus(String email) {
		Cart cart = getActiveCart(email);

		cart.setStatus(CartStatus.ORDERED);
		Cart cartUpdated = cartRepository.save(cart);

		return new MessageResDto("Carrito ordenado de forma exitosa", HttpStatus.OK.value());
	}

	@Override
	public Cart getActiveCart(String email) {
		User user = userService.findByEmail(email);

		Optional<Cart> cart = cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE);

		if (cart.isEmpty()) throw new NotFoundException("No cuenta con un carrito activo");

		return cart.get();
	}

	@Override
	public void updateTotalAmount(Cart cart, BigDecimal totalAmount) {
		cart.setTotalAmount(totalAmount);
		Cart cartUpdated = cartRepository.save(cart);
		logger.info("Carrito {} actualizado", cartUpdated.getId());
	}


}
