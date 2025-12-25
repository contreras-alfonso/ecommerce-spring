package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProfileRequestDto;
import org.alfonso.ecommerce.dto.ProfileResponseDto;
import org.alfonso.ecommerce.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> findProfile() {
        return ResponseEntity.ok(profileService.findProfile());
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> update(
            @Valid @RequestBody ProfileRequestDto request
    ) {
        return ResponseEntity.ok(profileService.update(request));
    }
}
