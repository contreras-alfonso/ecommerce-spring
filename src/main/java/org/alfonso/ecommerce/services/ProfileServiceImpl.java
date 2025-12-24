package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProfileResponseDto;
import org.alfonso.ecommerce.entities.User;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.UserNotFoundException;
import org.alfonso.ecommerce.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public ProfileResponseDto findProfile() {
        User user = userRepository.findById(jwtService.extractId()).orElseThrow(
                () -> new UserNotFoundException("No se pudo encontrar el usuario"));

        ProfileResponseDto responseDto = new ProfileResponseDto();
        responseDto.setName(user.getName());
        responseDto.setLastname(user.getLastname());
        responseDto.setEmail(user.getEmail());
        responseDto.setPhone(user.getPhone());
        responseDto.setDocumentType(user.getDocumentType());
        responseDto.setDocumentNumber(user.getDocumentNumber());

        return responseDto;
    }
}
