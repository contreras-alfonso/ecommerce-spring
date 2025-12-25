package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProfileRequestDto;
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

    @Override
    public ProfileResponseDto update(ProfileRequestDto request) {
        User user = userRepository.findById(jwtService.extractId()).orElseThrow(
                () -> new UserNotFoundException("No se pudo encontrar el usuario"));

        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setDocumentType(request.getDocumentType());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        ProfileResponseDto responseDto = new ProfileResponseDto();
        responseDto.setName(updatedUser.getName());
        responseDto.setLastname(updatedUser.getLastname());
        responseDto.setEmail(updatedUser.getEmail());
        responseDto.setPhone(updatedUser.getPhone());
        responseDto.setDocumentType(updatedUser.getDocumentType());
        responseDto.setDocumentNumber(updatedUser.getDocumentNumber());

        return responseDto;

    }
}
