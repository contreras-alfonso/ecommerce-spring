package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.CartResponseDto;
import org.alfonso.ecommerce.dto.FindActiveCartRequest;
import org.alfonso.ecommerce.dto.RemoveItemCartRequest;
import org.alfonso.ecommerce.dto.VerifyStockRequest;


public interface CartService {
    CartResponseDto checkStockAndUpdateCart(VerifyStockRequest stockRequest);

    CartResponseDto removeItemFromCart(RemoveItemCartRequest removeItemCartRequest);

    CartResponseDto findActiveCartFromUser();

    CartResponseDto findActiveCart(FindActiveCartRequest request);

}
