package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.ShippingAddress;
import org.alfonso.ecommerce.entities.User;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.UserNotFoundException;
import org.alfonso.ecommerce.repositories.ShippingAddressRepository;
import org.alfonso.ecommerce.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl implements ShippingAddressService {
    private final UserRepository userRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public List<ShippingAddress> findAll() {
        return shippingAddressRepository.getAllByUserIdAndIsDeletedFalse(jwtService.extractId());
    }

    @Override
    @Transactional
    public ShippingAddress save(ShippingAddress shippingAddress) {
        User user = userRepository.findById(jwtService.extractId()).orElseThrow(
                () -> new UserNotFoundException("El usuario no pudo ser encontrado"));

        ensureSingleDefaultShippingAddress(shippingAddress);

        user.addShippingAddress(shippingAddress);
        userRepository.save(user);
        return shippingAddress;
    }

    @Override
    @Transactional
    public ShippingAddress update(String addressId, ShippingAddress shippingAddress) {
        ShippingAddress shippingAddressDb = shippingAddressRepository.
                findByIdAndUserIdAndIsDeletedFalse(addressId, jwtService.extractId()).orElseThrow(
                        () -> new EntityNotFoundException("La dirección no pudo ser encontrada"));

        ensureSingleDefaultShippingAddress(shippingAddress);

        shippingAddressDb.setAddress(shippingAddress.getAddress());
        shippingAddressDb.setReference(shippingAddress.getReference());
        shippingAddressDb.setPhone(shippingAddress.getPhone());
        shippingAddressDb.setUbigeo(shippingAddress.getUbigeo());
        shippingAddressDb.setDefault(shippingAddress.isDefault());
        shippingAddressDb.setLat(shippingAddress.getLat());
        shippingAddressDb.setLng(shippingAddress.getLng());

        shippingAddressRepository.save(shippingAddressDb);
        return shippingAddressDb;

    }

    @Override
    @Transactional
    public void delete(String addressId) {
        ShippingAddress shippingAddressDb = shippingAddressRepository.
                findByIdAndUserIdAndIsDeletedFalse(addressId, jwtService.extractId()).orElseThrow(
                        () -> new EntityNotFoundException("La dirección no pudo ser encontrada"));

        shippingAddressDb.setDefault(false);
        shippingAddressDb.setDeleted(true);
        shippingAddressRepository.save(shippingAddressDb);

        List<ShippingAddress> addresses = shippingAddressRepository.getAllByUserIdAndIsDeletedFalse(jwtService.extractId());

        if (!addresses.isEmpty()) {
            ShippingAddress firstAddress = addresses.get(0);
            firstAddress.setDefault(true);
            shippingAddressRepository.save(firstAddress);
        }
    }

    private void ensureSingleDefaultShippingAddress(ShippingAddress shippingAddress) {
        long countAddresses = shippingAddressRepository.countByUserIdAndIsDeletedFalse(jwtService.extractId());
        // Si no tiene direcciones por defecto, hacer la actual por defecto
        if (countAddresses == 0 && !shippingAddress.isDefault()) {
            shippingAddress.setDefault(true);
        }
        // Si es dirección por defecto
        if (shippingAddress.isDefault()) {
            // Verificar si ya existe alguna dirección por defecto
            Optional<ShippingAddress> shippingAddressOptional = shippingAddressRepository
                    .getByIsDefaultTrueAndUserIdAndIsDeletedFalse(jwtService.extractId());
            if (shippingAddressOptional.isPresent()) {
                // Actualizar el isDefault a false
                ShippingAddress shippingAddressDb = shippingAddressOptional.get();
                shippingAddressDb.setDefault(false);
                shippingAddressRepository.save(shippingAddressDb);
            }
        }

    }
}
