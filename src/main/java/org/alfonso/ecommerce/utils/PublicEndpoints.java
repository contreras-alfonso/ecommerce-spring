package org.alfonso.ecommerce.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.regex.Pattern;

public class PublicEndpoints {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/**"
    );

    private static final List<Pattern> PUBLIC_GET_PATTERNS = List.of(
            Pattern.compile("^/api/categories(/[^/]+)?$"),
            Pattern.compile("^/api/brands(/[^/]+)?$"),
            Pattern.compile("^/api/colors(/[^/]+)?$"),
            Pattern.compile("^/api/products(/[^/]+)?$"),
            Pattern.compile("^/api/products/by/[^/]+$")
    );

    private static final List<Pattern> PUBLIC_POST_PATTERNS = List.of(
            Pattern.compile("^/api/cart(/.*)?$")
    );

    private PublicEndpoints() {
    }

    public static boolean isPublic(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p.replace("/**", ""))) {
                return true;
            }
        }
        if (HttpMethod.GET.matches(method)) {
            return PUBLIC_GET_PATTERNS.stream()
                    .anyMatch(pattern -> pattern.matcher(path).matches());
        }

        if (HttpMethod.POST.matches(method)) {
            return PUBLIC_POST_PATTERNS.stream()
                    .anyMatch(pattern -> pattern.matcher(path).matches());
        }

        return false;
    }
}