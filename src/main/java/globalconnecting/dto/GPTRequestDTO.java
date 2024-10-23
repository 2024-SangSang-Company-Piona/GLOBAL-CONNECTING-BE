package globalconnecting.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTRequestDTO {
    private String model;
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;   // "user", "assistant", or "system"
        private String content;  // User's question or assistant's response
    }
}
