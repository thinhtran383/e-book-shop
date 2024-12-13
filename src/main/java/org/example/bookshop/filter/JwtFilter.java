package org.example.bookshop.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.components.JwtGenerator;
import org.example.bookshop.services.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Value("${api.base-path}")
    private String apiPrefix;

    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isNonAuthRequest(request)) {
            filterChain.doFilter(request, response);
            log.info("Non-auth request");
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization header: {}", authorizationHeader);

        if (!isValidAuthorizationHeader(authorizationHeader, response)) {
            return;
        }

        final String token = authorizationHeader.substring(7);

        if (!validateToken(token, response)) {
            return;
        }

        authenticate(token, request, response);

        filterChain.doFilter(request, response);
    }

    private boolean isNonAuthRequest(HttpServletRequest request) {
        final List<Pair<String, String>> nonAuthEndpoints = List.of(
                Pair.of("/swagger-ui", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/books/details/**", apiPrefix), "GET"),
                Pair.of("/actuator/health/**", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        return nonAuthEndpoints.stream()
                .anyMatch(pair -> requestPath.matches(pair.getFirst().replace("**", ".*"))
                        && requestMethod.equalsIgnoreCase(pair.getSecond()));
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader, HttpServletResponse response) throws IOException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is missing or invalid.");
            return false;
        }
        return true;
    }

    private boolean validateToken(String token, HttpServletResponse response) throws IOException {
        if (tokenService.isTokenExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is expired.");
            return false;
        }

        if (!jwtGenerator.isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
            return false;
        }
        return true;
    }

    private void authenticate(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = jwtGenerator.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
        }
    }
}
