package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    List<Color> findAll();

    Optional<Color> findById(String id);

    Color save(Color color);

    Optional<Color> update(String id, Color color);

}
