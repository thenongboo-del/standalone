package com.sbproject.standalone.service;

import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sbproject.standalone.entity.ChatRequestDto;
import com.sbproject.standalone.entity.ChatResponseDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class FastApiClientService {

    private final WebClient fastApiWebClient;

    public Mono<ChatResponseDto> sendMessage(String message, String sessionId) {
        ChatRequestDto req = new ChatRequestDto(message, sessionId);

        return fastApiWebClient.post()
                .uri("/chat")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ChatResponseDto.class)
                .timeout(Duration.ofSeconds(20)) // 서버 응답 타임아웃
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(1))) // 간단 재시도
                .onErrorMap(ex -> new RuntimeException("FastAPI 호출 실패: " + ex.getMessage(), ex));
    }
}