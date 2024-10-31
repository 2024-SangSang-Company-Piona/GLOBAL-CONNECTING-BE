package globalconnecting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private T results;
    //T는 List<MemberDto> 같은 복합적인 타입도 받을 수 있음!!
}
