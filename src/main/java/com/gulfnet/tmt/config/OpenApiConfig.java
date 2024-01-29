package com.gulfnet.tmt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Gulfnet TMT API",
                version = "1.1",
                description = "API documentation for Gulfnet TMT services"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8084/gulfnet-tmt"
                ),
                @Server(
                        description = "AWS DEV ENV",
                        url = "http://18.136.236.178:8084/gulfnet-tmt"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
