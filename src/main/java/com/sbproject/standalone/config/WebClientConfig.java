package com.sbproject.standalone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000") // FastAPI 주소
                .defaultHeader("Content-Type", "application/json")
                // 필요하면 인증 토큰 헤더 추가
                // .defaultHeader("Authorization", "Bearer ...")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }
}