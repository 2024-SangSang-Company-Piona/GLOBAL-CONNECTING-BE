package globalconnecting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ChatConfig {
    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public WebClient GPTClient(){
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + apiKey)  // Authorization 헤더 추가
                .defaultHeader("Content-Type", "application/json")  // Content-Type 헤더 추가
                .build();
    }
}