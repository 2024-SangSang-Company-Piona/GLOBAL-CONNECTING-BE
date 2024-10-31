package globalconnecting.service;

import globalconnecting.domain.Chatting;
import globalconnecting.domain.Member;
import globalconnecting.domain.Message;
import globalconnecting.dto.MessageRequestDTO;
import globalconnecting.dto.gpt.GPTRequestDTO;
import globalconnecting.dto.gpt.GPTResponseDTO;
import globalconnecting.repository.ChattingRepository;
import globalconnecting.repository.MemberRepository;
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
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final WebClient GPTClient;
    private final ChattingRepository chattingRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.role}")
    private String role;
    @Value("${openai.content}")
    private String content;

    // GPTRequestDTO는 그냥 질문 하나를 받아오는 용도, List에 얘를 넣어서 지피티한테 요청
    public Mono<GPTResponseDTO> askQuestion(MessageRequestDTO messageRequestDTO, Long memberId, Long chatId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new NoSuchElementException("해당 회원은 존재하지 않습니다"));
        // 채팅이 있나 없나 확인, 없으면 생성
        Chatting chatting = ChattingIsNull(chatId);
        chatting.addChatting(member);
        // GPT 요청에 들어갈 dto 생성, messageDto에서 user와 내용만 받아올거니까 이미 만들어두자
        GPTRequestDTO gptRequestDTO = new GPTRequestDTO();
        gptRequestDTO.setModel(model);
        GPTRequestDTO.Message systemMessage = new GPTRequestDTO.Message(role,content);
        GPTRequestDTO.Message userMessage = new GPTRequestDTO.Message("user",messageRequestDTO.getContent());

        // 채팅방에 있는 모든 메시지를 가져온다, 메시지를 가져와서 requestDTO의 메세지에 리스트를 전달해야한다
        List<Message> messageList = getAllMessage(chatting, systemMessage, userMessage);

        // 요청 DTO의 메시지를 추가할때 messageDTO가 dto의 맨 아래로 오게한다
        List<GPTRequestDTO.Message> requestMessage = new ArrayList<>();
        for (Message message : messageList) {
            GPTRequestDTO.Message AddMessage= new GPTRequestDTO.Message(message.getRole(), message.getContent());
            log.info("히스토리 질문 모음: {}", AddMessage); // 로그로 기록
            requestMessage.add(AddMessage);
        }
       gptRequestDTO.setMessages(requestMessage);
        // 3. WebClient를 사용해 GPT에 요청을 보낸 후 응답 처리
        return GPTClient.post()
                .uri("/completions")
                .bodyValue(gptRequestDTO)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class)
                .doOnSuccess(gptResponse -> {
                    // 4. GPT 응답 메시지를 가져와 Message 엔티티로 변환
                    GPTResponseDTO.Message gptMessage = gptResponse.getChoices().get(0).getMessage(); // 첫 번째 응답
                    Message responseMessage = Message.createMessage(gptMessage.getRole(), gptMessage.getContent(), chatting);
                    log.info("GPT 답변 : {}",gptResponse);
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
        if(chatId == null){
            Chatting newChat = Chatting.createChatting();
            chattingRepository.save(newChat);
            return newChat;
        }
        else return chattingRepository.findById(chatId).orElseThrow(()-> new NoSuchElementException("채팅 아이디 없음"));
    }

    // 메시지 리스트 만들기 0번 인덱스는 시스템, 1번 인덱스가 내가 보낼 정보
    public List<Message> getAllMessage(Chatting chatting, GPTRequestDTO.Message system, GPTRequestDTO.Message user){
        List<Message> messageList = messageRepository.findAllByChatting(chatting);
        Message Systemmessage = Message.createMessage(system.getRole(), system.getContent(), chatting);
        Message Usermessage = Message.createMessage(user.getRole(),user.getContent(), chatting);
        Systemmessage.addMessages(chatting);
        Usermessage.addMessages(chatting);
        messageRepository.save(Systemmessage);
        messageRepository.save(Usermessage);
        messageList.add(Systemmessage);
        messageList.add(Usermessage);
        return messageList;
    }
}
