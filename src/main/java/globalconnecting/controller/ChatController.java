package globalconnecting.controller;

import globalconnecting.dto.MessageRequestDTO;
import globalconnecting.dto.gpt.GPTRequestDTO;
import globalconnecting.dto.gpt.GPTResponseDTO;
import globalconnecting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/ask/{memberId}")
    public Mono<GPTResponseDTO> askQuestion(@RequestBody MessageRequestDTO messageRequestDTO , @PathVariable Long memberId, @RequestParam(required = false) Long chatId){
        return chatService.askQuestion(messageRequestDTO, memberId, chatId);
    }
}
