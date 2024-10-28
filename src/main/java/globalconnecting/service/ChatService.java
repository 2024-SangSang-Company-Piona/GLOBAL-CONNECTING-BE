package globalconnecting.service;

import globalconnecting.domain.Chatting;
import globalconnecting.domain.Message;
import globalconnecting.dto.GPTRequestDTO;
import globalconnecting.dto.GPTResponseDTO;
import globalconnecting.repository.ChattingRepository;
import globalconnecting.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final WebClient GPTClient;
    private final ChattingRepository chattingRepository;
    private final MessageRepository messageRepository;
    @Value("${openai.model}")
    private String model;
    // GPTRequestDTO는 그냥 질문 하나를 받아오는 용도, List에 얘를 넣어서 지피티한테 요청
    public Mono<GPTResponseDTO> askQuestion(GPTRequestDTO gptRequestDTO, Long chatId) {
        Chatting chatting = ChattingIsNull(chatId);
        // 채팅방에 있는 모든 메시지를 가져온다, 메시지를 가져와서 requestDTO의 메세지에 리스트를 전달해야한다
        List<Message> messageList = getAllMessage(chatting, gptRequestDTO);
        // 요청 DTO의 메시지를 추가할때 messageDTO가 dto의 맨 아래로 오게한다
        List<GPTRequestDTO.Message> requestMessage = new ArrayList<>();
        for (Message message : messageList) {
            GPTRequestDTO.Message AddMessage= new GPTRequestDTO.Message(message.getRole(), message.getContent());
            log.info("히스토리 질문 모음: {}", AddMessage); // 로그로 기록
            requestMessage.add(AddMessage);
        }
        GPTRequestDTO AllRequestDTO = new GPTRequestDTO(model, requestMessage);
        // 3. WebClient를 사용해 GPT에 요청을 보낸 후 응답 처리
        return GPTClient.post()
                .uri("/completions")
                .bodyValue(AllRequestDTO)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class)
                .doOnSuccess(gptResponse -> {
                    // 4. GPT 응답 메시지를 가져와 Message 엔티티로 변환
                    GPTResponseDTO.Message gptMessage = gptResponse.getChoices().get(0).getMessage(); // 첫 번째 응답
                    Message responseMessage = Message.createMessage(gptMessage.getRole(), gptMessage.getContent(), chatting);

                    // 5. 응답 메시지를 chatting과 연관짓고, messageList 및 repository에 추가
                    responseMessage.addMessages(chatting);
                    messageRepository.save(responseMessage);
                    messageList.add(responseMessage);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("Error: " + e.getResponseBodyAsString());
                    return Mono.empty();
                });
    }
    // 채팅 있나 없나 확인하기
    public Chatting ChattingIsNull(Long chatId) {
        Chatting chatting = chattingRepository.findById(chatId).orElse(null);
        if(chatting == null){
            Chatting newChat = Chatting.createChatting();
            chattingRepository.save(newChat);
            return newChat;
        }
        return chatting;
    }

    // 메시지 리스트 만들기 0번 인덱스는 시스템, 1번 인덱스가 내가 보낼 정보
    public List<Message> getAllMessage(Chatting chatting, GPTRequestDTO gptRequestDTO){
        List<Message> messageList = messageRepository.findAllByChatting(chatting);
        Message Systemmessage = Message.createMessage(gptRequestDTO.getMessages().get(0).getRole(),gptRequestDTO.getMessages().get(0).getContent(), chatting);
        Message Usermessage = Message.createMessage(gptRequestDTO.getMessages().get(1).getRole(),gptRequestDTO.getMessages().get(1).getContent(), chatting);
        Systemmessage.addMessages(chatting);
        Usermessage.addMessages(chatting);
        messageRepository.save(Systemmessage);
        messageRepository.save(Usermessage);
        messageList.add(Systemmessage);
        messageList.add(Usermessage);
        return messageList;
    }
}
