package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Brand;
import org.alfonso.ecommerce.exceptions.ResourceConflictException;
import org.alfonso.ecommerce.repositories.BrandRepository;
import org.alfonso.ecommerce.utils.GeneralUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Brand> findById(String id) {
        return brandRepository.findById(id);
    }

    @Override
    @Transactional
    public Brand save(Brand brand) {
        if (brandRepository.existsByNameIgnoreCase(brand.getName())) {
            throw new ResourceConflictException("El nombre de la marca ya existe.");
        }

        String baseSlug = GeneralUtil.generateSlug(brand.getName());
        String finalSlug = baseSlug;
        int count = 1;

        // Si el slug existe, agregar un numero adicional
        while (brandRepository.existsBySlugIgnoreCase(finalSlug)) {
            finalSlug = baseSlug + "-" + count;
            count++;
        }

        brand.setSlug(finalSlug);
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public Optional<Brand> update(String id, Brand brand) {
        return brandRepository.findById(id).map(brandDb -> {
            brandDb.setName(brand.getName());
            return brandRepository.save(brandDb);
        });
    }
}
