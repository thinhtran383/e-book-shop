package org.example.bookshop.config;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(
//                        requests -> {
//                            requests
//                                    .requestMatchers(
//                                            // swagger
//                                            "/swagger-ui/**",
//                                            "/v3/api-docs/",
//                                            "/v3/api-docs/**",
//                                            "/api-docs",
//                                            "/api-docs/**",
//                                            "/swagger-resources",
//                                            "/swagger-resources/**",
//                                            "/configuration/ui",
//                                            "/configuration/security",
//                                            "/swagger-ui/**",
//                                            "/swagger-ui.html"
//                                    )
//                                    .permitAll();
//                        }
//                )
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .authenticationProvider(authenticationProvider);


        return http.build();
    }
}