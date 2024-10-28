package globalconnecting.repository;

import globalconnecting.domain.Chatting;
import globalconnecting.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAllByChatting(Chatting chatting);
}
