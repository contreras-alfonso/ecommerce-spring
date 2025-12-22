package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.VerifyStockRequest;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.NoStockAvailableException;
import org.alfonso.ecommerce.repositories.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional(readOnly = true)
    public void verifyStock(VerifyStockRequest stockRequest) {

        if (stockRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Integer currentStock = productVariantRepository.findStockById(stockRequest.getVariantId());

        if (currentStock == null) {
            throw new EntityNotFoundException("No se pudo encontrar la variante");
        }
        if (currentStock < stockRequest.getQuantity()) {
            throw new NoStockAvailableException("La cantidad solicitada supera el stock disponible.");
        }
        ;
    }
}
