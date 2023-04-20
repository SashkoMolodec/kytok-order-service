package com.kytokvinily.vinyl;

import com.kytokvinily.config.VinylClientProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;

@TestMethodOrder(MethodOrderer.Random.class)
class VinylClientTests {

    private MockWebServer mockWebServer;
    private VinylClient vinylClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        VinylClientProperties properties = new VinylClientProperties(null,
                Duration.ofSeconds(1),
                Duration.ZERO,
                1);

        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();
        this.vinylClient = new VinylClient(webClient, properties);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void whenVinylExistsThenReturnVinyl() {
        var vinylId = 1L;

        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        	{
                              "id": 1,
                              "title": "Some serious shite 2",
                              "year": 1992,
                              "author": "shit author2"
                          	}
                        """);

        mockWebServer.enqueue(mockResponse);

        Mono<VinylDto> vinylDto = vinylClient.getVinylById(vinylId);

        StepVerifier.create(vinylDto)
                .expectNextMatches(vin -> vin.getId() == vinylId)
                .verifyComplete();
    }

    @Test
    void whenVinylNotExistsThenReturnEmpty() {
        var vinylId = 1L;

        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404);

        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(vinylClient.getVinylById(vinylId))
                .expectNextCount(0)
                .verifyComplete();
    }

}