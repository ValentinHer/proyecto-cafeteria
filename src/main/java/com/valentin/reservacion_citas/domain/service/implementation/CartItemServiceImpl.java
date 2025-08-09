package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.CartItemMapper;
import com.valentin.reservacion_citas.domain.service.CartItemService;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.persistence.entity.CartItem;
import com.valentin.reservacion_citas.persistence.repository.CartItemRepository;
import com.valentin.reservacion_citas.persistence.repository.CartRepository;
import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.response.CartItemResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemServiceImpl implements CartItemService {
	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final CartItemMapper cartItemMapper;
	private final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

	public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, CartService cartService, CartItemMapper cartItemMapper) {
		this.cartItemRepository = cartItemRepository;
		this.cartRepository = cartRepository;
		this.cartService = cartService;
		this.cartItemMapper = cartItemMapper;
	}

	@Override
	@Transactional
	public MsgDataResDto<CartItemResDto> addToCart(CartItemReqDto cartItemReqDto, String email) {
		Cart cart = cartService.createOrRetrieveActiveCartByUser(email);

		BigDecimal total = cartItemReqDto.getUnitaryPrice().multiply(BigDecimal.valueOf(cartItemReqDto.getQuantity()));
		cartItemReqDto.setTotalAmount(total);

		CartItem cartItem = cartItemMapper.toEntity(cartItemReqDto, cart);
		CartItem cartItemSaved = cartItemRepository.save(cartItem);
		logger.info("Producto {} guardado", cartItemSaved.getId());

		cart.setTotalAmount(cart.getTotalAmount().add(cartItemReqDto.getTotalAmount()));
		Cart cartUpdated = cartRepository.save(cart);
		logger.info("Carrito {} actualizado", cartUpdated.getId());

		MsgDataResDto<CartItemResDto> response = new MsgDataResDto<>();
		response.setMessage("Producto agregado al carrito");
		response.setStatus(MsgStatus.SUCCESS);
		response.setData(cartItemMapper.toResponse(cartItemSaved, false));

		return response;
	}


}
