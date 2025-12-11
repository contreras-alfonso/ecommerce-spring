package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.services.SeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seed")
@RequiredArgsConstructor
public class SeedController {
    private final SeedService seedService;

    @GetMapping
    public String seed() {
        seedService.seed();
        return "OK";
    }
}
