package com.kytokvinily.vinyl;

import com.kytokvinily.config.VinylClientProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class VinylClient {
    public static final String VINYLS_ROOT_API = "/vinyls/";
    private final WebClient webClient;
    private final VinylClientProperties vinylClientProperties;

    public VinylClient(WebClient webClient, VinylClientProperties vinylClientProperties) {
        this.webClient = webClient;
        this.vinylClientProperties = vinylClientProperties;
    }

    public Mono<VinylDto> getVinylById(Long id) {
        return webClient
                .get()
                .uri(VINYLS_ROOT_API + id)
                .retrieve()
                .bodyToMono(VinylDto.class)
                .timeout(vinylClientProperties.timeout(), Mono.empty())
                .retryWhen(
                        Retry.backoff(vinylClientProperties.retryMaxAttempts(),
                                vinylClientProperties.retryInitialBackoff())
                );
    }
}
