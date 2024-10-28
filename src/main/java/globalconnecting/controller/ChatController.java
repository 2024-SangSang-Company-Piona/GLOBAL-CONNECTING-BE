package globalconnecting.controller;

import globalconnecting.dto.GPTRequestDTO;
import globalconnecting.dto.GPTResponseDTO;
import globalconnecting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/ask/{chatId}")
    public Mono<GPTResponseDTO> askQuestion(@RequestBody GPTRequestDTO gptRequestDTO, @PathVariable Long chatId){
        return chatService.askQuestion(gptRequestDTO, chatId);
    }

    @PostMapping("/{chatId}")
    public void makeChatting(@PathVariable Long chatId){
        chatService.ChattingIsNull(chatId);
    }
}
