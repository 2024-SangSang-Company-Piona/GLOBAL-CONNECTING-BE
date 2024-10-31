package globalconnecting.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chatting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ChattingId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "chatting")
    private List<Message> messageList = new ArrayList<>();

    public static Chatting createChatting(){
        Chatting chatting = new Chatting();
        return chatting;
    }

    public void addChatting(Member member){
        this.member = member;
        member.getChattings().add(this);
    }

    public void setSummaryTitle(String title){
        this.title = title;
    }
}
