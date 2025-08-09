package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.CartMapper;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.persistence.entity.CartStatus;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.CartRepository;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final UserService userService;
	private final CartMapper cartMapper;

	public CartServiceImpl(CartRepository cartRepository, UserService userService, CartMapper cartMapper) {
		this.cartRepository = cartRepository;
		this.userService = userService;
		this.cartMapper = cartMapper;
	}

	@Override
	public Cart createOrRetrieveActiveCartByUser(String email) {
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
	@Transactional
	public MsgDataResDto<CartResDto> getCartWithItems(String email) {
		Cart cartFound = createOrRetrieveActiveCartByUser(email);

		CartResDto cartResDto = cartMapper.toResponse(cartFound);

		MsgDataResDto<CartResDto> response = new MsgDataResDto<>();
		response.setStatus(MsgStatus.SUCCESS);
		response.setMessage("Carrito Activo Recuperado Exitosamente");
		response.setData(cartResDto);

		return response;
	}
}
