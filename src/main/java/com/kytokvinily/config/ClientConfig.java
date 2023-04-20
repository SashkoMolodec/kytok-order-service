package com.kytokvinily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    WebClient webClient(VinylClientProperties vinylClientProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(vinylClientProperties.uri().toString()).build();
    }
}
