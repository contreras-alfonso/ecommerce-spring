package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.entities.Cart;
import org.alfonso.ecommerce.entities.CartItem;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.NoStockAvailableException;
import org.alfonso.ecommerce.repositories.CartRepository;
import org.alfonso.ecommerce.repositories.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final ProductVariantRepository productVariantRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public Cart checkStockAndUpdateCart(VerifyStockRequest stockRequest) {

        if (stockRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Si el ID del carrito no es null, buscarlo en la DB
        if (stockRequest.getCartId() != null) {
            Cart cartDb = cartRepository.findById(stockRequest.getCartId()).orElseThrow(() ->
                    new EntityNotFoundException("Carrito no encontrado"));

            Optional<CartItem> findCartItem = cartDb.getCartItems().stream().filter(cartItem ->
                    cartItem.getVariantId().equals(stockRequest.getVariantId())).findFirst();

            // Si el item del carrito ya existía, validar por su el stock de la db + stock request
            if (findCartItem.isPresent()) {
                CartItem cartItemDb = findCartItem.get();
                Integer totalQuantity = cartItemDb.getQuantity() + stockRequest.getQuantity();
                verifyStock(stockRequest.getVariantId(), totalQuantity);
                cartItemDb.setQuantity(totalQuantity);
            } else {
                // Si el item no existía crearlo
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(stockRequest.getQuantity());
                cartItem.setVariantId(stockRequest.getVariantId());
                cartDb.addItem(cartItem);
            }
            return cartRepository.save(cartDb);

        } else {  // Si el ID del carrito es null crear uno nuevo en la DB

            //Validar stock solicitado
            verifyStock(stockRequest.getVariantId(), stockRequest.getQuantity());

            // Generar nuevo Cart
            CartItem item = new CartItem();
            item.setVariantId(stockRequest.getVariantId());
            item.setQuantity(stockRequest.getQuantity());

            Cart cart = new Cart();
            cart.addItem(item);
            return cartRepository.save(cart);
        }

    }

    private void verifyStock(String variantId, Integer requestedQuantity) {
        Integer currentStock = productVariantRepository.findStockById(variantId);

        if (currentStock == null) {
            throw new EntityNotFoundException("No se pudo encontrar la variante");
        }
        if (currentStock < requestedQuantity) {
            throw new NoStockAvailableException("La cantidad solicitada supera el stock disponible.");
        }
    }
}
