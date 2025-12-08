package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();

    Optional<Brand> findById(String id);

    Brand save(Brand brand);

    Optional<Brand> update(String id, Brand brand);

}
