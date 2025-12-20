package org.alfonso.ecommerce.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.exceptions.InvalidJwtTokenException;
import org.alfonso.ecommerce.exceptions.MissingAuthorizationHeaderException;
import org.alfonso.ecommerce.services.JwtService;
import org.alfonso.ecommerce.utils.JwtErrorResponseWriter;
import org.alfonso.ecommerce.utils.PublicEndpoints;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {

            if (PublicEndpoints.isPublic(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new MissingAuthorizationHeaderException("Authorization header missing or malformed");
            }

            String jwt = getJwtFormRequest(request);

            if (!jwtService.isValidToken(jwt)) {
                throw new InvalidJwtTokenException("Invalid token");
            }

            String username = jwtService.extractUsernameFromToken(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateTokenForUser(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (MissingAuthorizationHeaderException ex) {
            JwtErrorResponseWriter.sendUnauthorized(response, "Token no proveído", ex.getMessage());
        } catch (InvalidJwtTokenException ex) {
            JwtErrorResponseWriter.sendUnauthorized(response, "Token inválido", ex.getMessage());
        }
    }


    private String getJwtFormRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        //Bearer <token>
        return authHeader.substring(7);
    }
}
