package com.valentin.reservacion_citas.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.request.ProductReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgErrorResDto;
import com.valentin.reservacion_citas.web.dto.response.ProductResDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
	MessageResDto create(ProductReqDto productReqDto);

	ProductResDto getOneById(String id, Boolean withCategory);

	List<ProductResDto> getAllActive();

	MessageResDto updateById(ProductReqDto productReqDto, String id);

	MessageResDto deleteById(String id);

	void validateStockProducts(List<CartItemReqDto>  products) throws JsonProcessingException;
}
