package org.alfonso.ecommerce.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.alfonso.ecommerce.dto.*;
import org.alfonso.ecommerce.entities.DocumentType;
import org.alfonso.ecommerce.entities.Role;
import org.alfonso.ecommerce.entities.User;
import org.alfonso.ecommerce.exceptions.InvalidRefreshToken;
import org.alfonso.ecommerce.exceptions.UserAlreadyExists;
import org.alfonso.ecommerce.exceptions.UserNotFoundException;
import org.alfonso.ecommerce.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExists("User already exists");
        }

        User user = User.builder().name(registerRequest.getName()).lastname(registerRequest.getLastname()).documentType(registerRequest.getDocumentType()).documentNumber(registerRequest.getDocumentNumber()).password(passwordEncoder.encode(registerRequest.getPassword())).email(registerRequest.getEmail()).role(Role.ROLE_USER).build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String email = jwtService.extractUsername(authentication);
            String name = jwtService.extractName(authentication);
            String lastName = jwtService.extractLastName(authentication);
            DocumentType documentType = jwtService.extractDocumentType(authentication);
            String documentNumber = jwtService.extractDocumentNumber(authentication);
            List<String> roles = jwtService.extractRoles(authentication);
            TokenPair tokenPair = jwtService.generateTokenPair(authentication);

            return new AuthResponse(email, name, lastName, documentType, documentNumber, roles, tokenPair);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    public AuthResponse refreshToken(@Valid RefreshTokenRequest request) {
        String refreshToken = request.getToken();

        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null) {
            throw new UserNotFoundException("User not found");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String email = jwtService.extractUsername(authentication);
        String name = jwtService.extractName(authentication);
        String lastName = jwtService.extractLastName(authentication);
        DocumentType documentType = jwtService.extractDocumentType(authentication);
        String documentNumber = jwtService.extractDocumentNumber(authentication);
        List<String> roles = jwtService.extractRoles(authentication);
        TokenPair tokenPair = jwtService.generateTokenPair(authentication);

        return new AuthResponse(email, name, lastName, documentType, documentNumber, roles, tokenPair);
    }
}
