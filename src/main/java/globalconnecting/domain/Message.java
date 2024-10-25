package globalconnecting.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MessageId;
    private String role;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatting chatting;

    /**
     * 연관관계 편의 메서드
     * @param chatting
     */
    public void addMessages(Chatting chatting){
        this.chatting = chatting;
        chatting.getMessageList().add(this);
    }
}
