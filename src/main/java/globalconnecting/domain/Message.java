package globalconnecting.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MessageId;
    private String role;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatting chatting;

    public static Message createMessage(String role, String content, Chatting chatting){
        Message message = new Message();
        message.role = role;
        message.content = content;
        message.chatting = chatting;
        return message;
    }
    /**
     * 연관관계 편의 메서드
     * @param chatting
     */
    public void addMessages(Chatting chatting){
        this.chatting = chatting;
        chatting.getMessageList().add(this);
    }
}
