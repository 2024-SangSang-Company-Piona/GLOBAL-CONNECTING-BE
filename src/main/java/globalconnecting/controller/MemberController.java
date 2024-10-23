package globalconnecting.controller;
import globalconnecting.dto.MemberRequestDto;
import globalconnecting.dto.MemberResponseDto;
import globalconnecting.dto.Result;
import globalconnecting.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("")
    public MemberResponseDto joinMember(@RequestBody MemberRequestDto requestDto){
        return memberService.join(requestDto);
    }

    @GetMapping("/findAllMember")
    public Result findAllMembers(){
        return memberService.findAll();
    }
}
