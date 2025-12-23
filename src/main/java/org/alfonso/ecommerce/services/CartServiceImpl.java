package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.CartItemDto;
import org.alfonso.ecommerce.dto.CartResponseDto;
import org.alfonso.ecommerce.dto.RemoveItemCartRequest;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.entities.*;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.InvalidJwtTokenException;
import org.alfonso.ecommerce.exceptions.NoStockAvailableException;
import org.alfonso.ecommerce.repositories.CartRepository;
import org.alfonso.ecommerce.repositories.ProductColorImageRepository;
import org.alfonso.ecommerce.repositories.ProductVariantRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductColorImageRepository productColorImageRepository;
    private final CartRepository cartRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public CartResponseDto checkStockAndUpdateCart(VerifyStockRequest stockRequest) {

        if (stockRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        boolean isAuthenticated = jwtService.isAuthenticated();


        // Si el ID del carrito no es null, buscarlo en la DB
        if (stockRequest.getCartId() != null) {
            Cart cartDb = cartRepository.findById(stockRequest.getCartId()).orElseThrow(() ->
                    new EntityNotFoundException("Carrito no encontrado"));


           /*if (!isAuthenticated && cartDb.getUserId() != null) {
                throw new InvalidJwtTokenException("No puedes realizar la acción correspondiente");
            }*/

            System.out.println("isAuthenticated = " + isAuthenticated);
            System.out.println("cartDb.getUserId() = " + cartDb.getUserId());
            System.out.println("jwtService.extractId() = " + jwtService.extractId());

            if (isAuthenticated && !cartDb.getUserId().equals(jwtService.extractId())) {
                throw new InvalidJwtTokenException("El token proporcionado es inválido o ha expirado qweq");
            }

            Optional<CartItem> findCartItem = cartDb.getCartItems().stream().filter(cartItem ->
                    cartItem.getVariantId().equals(stockRequest.getVariantId())).findFirst();

            // Si el item del carrito ya existía, validar por su el stock de la db + stock request
            if (findCartItem.isPresent()) {
                CartItem cartItemDb = findCartItem.get();
                Integer totalQuantity = 0;
                if (stockRequest.getMode() == StockUpdateMode.ADD) {
                    totalQuantity = stockRequest.getQuantity() + cartItemDb.getQuantity();
                } else {
                    totalQuantity = stockRequest.getQuantity();
                }

                verifyStock(stockRequest.getVariantId(), totalQuantity);
                cartItemDb.setQuantity(totalQuantity);
            } else {
                // Si el item no existía crearlo
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(stockRequest.getQuantity());
                cartItem.setVariantId(stockRequest.getVariantId());
                cartDb.addItem(cartItem);
            }
            if (isAuthenticated) {
                cartDb.setUserId(jwtService.extractId());
            }
            Cart savedCart = cartRepository.save(cartDb);
            return getCartResponseDto(savedCart);

        } else {  // Si el ID del carrito es null crear uno nuevo en la DB

            //Validar stock solicitado
            verifyStock(stockRequest.getVariantId(), stockRequest.getQuantity());

            // Generar nuevo Cart
            CartItem item = new CartItem();
            item.setVariantId(stockRequest.getVariantId());
            item.setQuantity(stockRequest.getQuantity());

            Cart cart = new Cart();
            if (isAuthenticated) {
                cart.setUserId(jwtService.extractId());
            }
            cart.addItem(item);
            Cart savedCart = cartRepository.save(cart);
            return getCartResponseDto(savedCart);
        }

    }

    @Override
    @Transactional
    public CartResponseDto removeItemFromCart(RemoveItemCartRequest removeItemCartRequest) {
        Cart cartDb = cartRepository.findById(removeItemCartRequest.getCartId()).orElseThrow(() ->
                new EntityNotFoundException("Carrito no encontrado"));

        CartItem cartItem = cartDb.getCartItems().stream().filter(item ->
                        item.getVariantId().equals(removeItemCartRequest.getVariantId())).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("El producto no fue encontrado en el carrito"));

        cartDb.removeItem(cartItem);
        Cart updatedCart = cartRepository.save(cartDb);
        return getCartResponseDto(updatedCart);

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

    private CartResponseDto getCartResponseDto(Cart cart) {


        Map<String, CartItem> cartItemMap = cart.getCartItems()
                .stream()
                .collect(Collectors.toMap(
                        CartItem::getVariantId,
                        item -> item
                ));

        List<String> variantIds = cart.getCartItems().stream().map(CartItem::getVariantId).toList();

        List<CartItemDto> items = new ArrayList<>();
        List<Object[]> variants = productVariantRepository.findCartVariants(variantIds);

        List<String> colorIds = variants.stream()
                .map(row -> ((ProductVariant) row[0]).getColor().getId())
                .distinct()
                .toList();

        List<ProductColorImage> images =
                productColorImageRepository.findFirstImageByColorIds(colorIds);

        Map<String, String> imageByColorId = images.stream()
                .collect(Collectors.toMap(
                        img -> img.getColor().getId(),
                        ProductColorImage::getUrl
                ));

        Double subtotal = 0.0;

        for (Object[] row : variants) {

            ProductVariant variant = (ProductVariant) row[0];
            Product product = (Product) row[1];
            CartItem cartItem = cartItemMap.get(variant.getId());
            String imageUrl = imageByColorId.get(variant.getColor().getId());

            CartItemDto dto = new CartItemDto();
            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setProductSlug(product.getSlug());

            dto.setVariantId(variant.getId());

            dto.setPrice(variant.getPrice());
            dto.setImageUrl(imageUrl);
            dto.setQuantity(cartItem.getQuantity());

            dto.setUsesTechnicalVariants(product.isUsesTechnicalVariants());

            dto.setRam(variant.getRam());
            dto.setStorage(variant.getStorage());

            items.add(dto);

            // Recalcular el subtotal
            subtotal += cartItem.getQuantity() * variant.getPrice();
        }

        CartResponseDto response = new CartResponseDto();
        response.setCartId(cart.getId());
        response.setItems(items);
        response.setSubtotal(subtotal);

        return response;

    }
}
