package org.alfonso.ecommerce.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Color;
import org.alfonso.ecommerce.repositories.ColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Color> findById(String id) {
        return colorRepository.findById(id);
    }

    @Override
    @Transactional
    public Color save(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public Optional<Color> update(String id, Color color) {
        return colorRepository.findById(id)
                .map(colorDb -> {
                    colorDb.setName(color.getName());
                    colorDb.setHex(color.getHex());
                    return colorRepository.save(colorDb);
                });
    }
}
