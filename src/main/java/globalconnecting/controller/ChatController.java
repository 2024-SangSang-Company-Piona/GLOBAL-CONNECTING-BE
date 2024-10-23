package globalconnecting.controller;

import globalconnecting.dto.GPTRequestDTO;
import globalconnecting.dto.GPTResponseDTO;
import globalconnecting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping()
    public Mono<GPTResponseDTO> askQuestion(@RequestBody GPTRequestDTO gptRequestDTO){
        return chatService.askQuestion(gptRequestDTO);
    }
}
