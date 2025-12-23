package org.alfonso.ecommerce.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.alfonso.ecommerce.details.CustomUserDetails;
import org.alfonso.ecommerce.dto.TokenPair;
import org.alfonso.ecommerce.entities.DocumentType;
import org.alfonso.ecommerce.exceptions.InvalidJwtTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;
    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    private static final String TOKEN_PREFIX = "Bearer ";

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }

    //Generar access token
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtExpirationMs, new HashMap<>());
    }

    //Generar refresh token
    public String generateRefreshToken(Authentication authentication) {

        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");

        return generateToken(authentication, refreshExpirationMs, claims);
    }

    private String generateToken(Authentication authentication, long expirationMs, Map<String, String> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal != null ? userPrincipal.getUsername() : null)
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    //Validar token
    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String username = this.extractUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());

    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null && !this.isRefreshToken(token);
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    //Validar si el token es refresh token
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) {
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            // TODO: aquí nunca devolverá un null
            throw new InvalidJwtTokenException("Invalid token");
        }
        return claims;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public List<String> extractRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", ""))
                .toList();
    }

    public String extractUsername(Authentication authentication) {
        CustomUserDetails userDetails = this.getUserDetails(authentication);
        return userDetails != null ? userDetails.getUsername() : "";
    }

    public String extractName(Authentication authentication) {
        CustomUserDetails userDetails = this.getUserDetails(authentication);
        return userDetails != null ? userDetails.getName() : "";
    }

    public String extractLastName(Authentication authentication) {
        CustomUserDetails userDetails = this.getUserDetails(authentication);
        return userDetails != null ? userDetails.getLastname() : "";
    }

    public String extractDocumentNumber(Authentication authentication) {
        CustomUserDetails userDetails = this.getUserDetails(authentication);
        return userDetails != null ? userDetails.getDocumentNumber() : "";
    }

    public DocumentType extractDocumentType(Authentication authentication) {
        CustomUserDetails userDetails = this.getUserDetails(authentication);
        return userDetails != null ? userDetails.getDocumentType() : DocumentType.DNI;
    }

    public String extractId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated()) {
            CustomUserDetails userDetails = this.getUserDetails(authentication);
            return userDetails != null ? userDetails.getId() : null;
        } else {
            return null;
        }

    }

    public CustomUserDetails getUserDetails(Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }


    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }


}
