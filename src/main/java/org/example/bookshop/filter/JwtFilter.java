package org.example.bookshop.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.entities.User;
import org.example.bookshop.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (nonAuthRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is missing or invalid.");
            return;
        }

        final String token = authorizationHeader.substring(7);
        final String username = jwtGenerator.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(username);


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

            authenticationToken.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean nonAuthRequest(HttpServletRequest request) {
        final List<Pair<String, String>> nonAuthEndpoints = List.of(
                Pair.of("/swagger-ui", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET"),

                // login
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/admin", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/forgot-password", apiPrefix), "POST"),

                Pair.of("/home", "GET"),
                Pair.of("/home/**", "GET"),

                Pair.of("/home", "POST"),
                Pair.of("/home/**", "POST")

        );


        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        return nonAuthEndpoints.stream()
                .anyMatch(pair -> requestPath.matches(pair.getFirst().replace("**", ".*"))
                        && requestMethod.equalsIgnoreCase(pair.getSecond()));
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        filterChain.doFilter(request, response);
//    }
}
