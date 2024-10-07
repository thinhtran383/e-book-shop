package org.example.bookshop.config;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${api.base-path}")
    private String apiPrefix;
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.oauth2Login(oauth2 ->
//                oauth2.successHandler((request, response, authentication) ->
//                        response.sendRedirect("/home/user-info"))
//        );


        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        requests -> {
                            requests
                                    .requestMatchers(HttpMethod.GET,
                                            // swagger
                                            "/swagger-ui/**",
                                            "/v3/api-docs/",
                                            "/v3/api-docs/**",
                                            "/api-docs",
                                            "/api-docs/**",
                                            "/swagger-resources",
                                            "/swagger-resources/**",
                                            "/configuration/ui",
                                            "/configuration/security",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",


                                            // comment
                                            String.format("%s/comments/**", apiPrefix),
                                            String.format("%s/comments", apiPrefix),

                                            "/home/**",

                                            // book
                                            String.format("%s/books/**", apiPrefix),
                                            String.format("%s/books", apiPrefix),

                                            // category
                                            String.format("%s/categories/**", apiPrefix),
                                            String.format("%s/categories", apiPrefix)


                                    )
                                    .permitAll();

                            requests.requestMatchers(HttpMethod.POST,
                                    String.format("%s/payments/ipn/**", apiPrefix)
                            ).permitAll();
                            requests.requestMatchers(String.format("%s/auth/**", apiPrefix))
                                    .permitAll();
                        }
                )

                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .authenticationProvider(authenticationProvider);


        return http.build();
    }
}
