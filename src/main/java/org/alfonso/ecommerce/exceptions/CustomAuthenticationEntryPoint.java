package org.alfonso.ecommerce.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.alfonso.ecommerce.utils.JwtErrorResponseWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        JwtErrorResponseWriter.sendError(
                response,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Token no proveído o inválido",
                authException.getMessage()
        );
    }
}
