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
import java.util.List;
import java.util.Optional;

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

	private MsgDataResDto<CartItemResDto> updateCartAndReturnItemData(Cart cart, CartItem cartItem, BigDecimal totalAmount) {
		cartService.updateTotalAmount(cart, totalAmount);

		MsgDataResDto<CartItemResDto> response = new MsgDataResDto<>();
		response.setMessage("Producto agregado al carrito");
		response.setStatus(MsgStatus.SUCCESS);
		response.setData(cartItemMapper.toResponse(cartItem, false));

		return response;
	}

	@Override
	@Transactional
	public MsgDataResDto<CartItemResDto> addToCart(CartItemReqDto cartItemReqDto, String email) {
		Cart cart = cartService.createOrGetActiveCartByUser(email);

		BigDecimal total = cartItemReqDto.getUnitaryPrice().multiply(BigDecimal.valueOf(cartItemReqDto.getQuantity()));
		cartItemReqDto.setTotalAmount(total);

		Optional<CartItem> cartItemExist = cartItemRepository.findByProductIdAndCartId(cartItemReqDto.getProductId(), cart.getId());
		if (cartItemExist.isPresent()) {
			CartItem cartItemFound = cartItemExist.get();

			cartItemFound.setTotalAmount(cartItemFound.getTotalAmount().add(cartItemReqDto.getTotalAmount()));
			cartItemFound.setQuantity(cartItemFound.getQuantity() + cartItemReqDto.getQuantity());

			CartItem cartItemUpdated = cartItemRepository.save(cartItemFound);
			logger.info("Producto {} actualizado", cartItemUpdated.getId());

			return updateCartAndReturnItemData(cart, cartItemUpdated, cart.getTotalAmount().add(cartItemReqDto.getTotalAmount()));
		}

		CartItem cartItem = cartItemMapper.toEntity(cartItemReqDto, cart.getId());
		CartItem cartItemSaved = cartItemRepository.save(cartItem);
		logger.info("Producto {} guardado", cartItemSaved.getId());

		return updateCartAndReturnItemData(cart, cartItemSaved, cart.getTotalAmount().add(cartItemSaved.getTotalAmount()));
	}

	@Override
	@Transactional
	public MessageResDto deleteItemFromCart(String cartItemId, String email) {
		Cart cart = cartService.createOrGetActiveCartByUser(email);

		List<CartItem> cartItems = cart.getProducts();

		if (cartItems.stream().noneMatch(cartItem -> cartItem.getId().equals(cartItemId))) throw new NotFoundException("El Item no se encuentra en el carrito");

		// Filtrar los que no coinciden con el itemId
		List<CartItem> cartItemList = cartItems.stream().filter(cartItem -> !cartItem.getId().equals(cartItemId)).toList();

		// Sumar los totales de los items filtrados anteriormente
		BigDecimal totalCartUpdated = cartItemList.stream().map(CartItem::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		cartItemRepository.deleteById(cartItemId);

		cartService.updateTotalAmount(cart, totalCartUpdated);

		return new MessageResDto("Item eliminado correctamente del carrito", HttpStatus.OK.value());
	}
}
