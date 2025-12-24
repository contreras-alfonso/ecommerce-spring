package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProfileResponseDto;
import org.alfonso.ecommerce.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> findProfile() {
        return ResponseEntity.ok(profileService.findProfile());
    }
}
