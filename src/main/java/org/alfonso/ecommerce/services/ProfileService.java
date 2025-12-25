package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.ProfileRequestDto;
import org.alfonso.ecommerce.dto.ProfileResponseDto;

import java.util.Optional;

public interface ProfileService {
    ProfileResponseDto findProfile();
    ProfileResponseDto update(ProfileRequestDto request);
}
