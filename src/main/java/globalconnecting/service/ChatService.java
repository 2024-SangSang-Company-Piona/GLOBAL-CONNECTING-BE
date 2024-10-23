package globalconnecting.service;

import globalconnecting.dto.GPTRequestDTO;
import globalconnecting.dto.GPTResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final WebClient GPTClient;

    public Mono<GPTResponseDTO> askQuestion(GPTRequestDTO requestDTO) {
        return GPTClient.post()
                .uri("/completions") // 엔드포인트만 지정
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error: " + e.getResponseBodyAsString());
                    return Mono.empty();
                });
    }
}
