package globalconnecting.dto;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter@AllArgsConstructor@NoArgsConstructor
public class MessageDTO {
    private String content;
    private String role;
}
