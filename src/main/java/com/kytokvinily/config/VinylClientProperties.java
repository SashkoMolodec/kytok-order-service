package com.kytokvinily.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties("kytok.vinyl-service")
public record VinylClientProperties(
        @NotNull URI uri,
        @NotNull Duration timeout,
        @NotNull Duration retryInitialBackoff,
        @NotNull Integer retryMaxAttempts
) {
}
