package org.example.bookshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        servers = {
                @Server(url = "https://api.thinhtran.online", description = "Server URL for production"),
        }
)
@Configuration
public class SwaggerConfig {
}
