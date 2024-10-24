package globalconnecting.service;

import globalconnecting.domain.Member;
import globalconnecting.dto.MemberRequestDto;
import globalconnecting.dto.MemberResponseDto;
import globalconnecting.dto.Result;
import globalconnecting.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto join(MemberRequestDto requestDto){
        Member member = Member.createMember(requestDto.getClass().getName());
        memberRepository.save(member);
        MemberResponseDto responseDto = new MemberResponseDto(member.getId());
        return responseDto;
    }

    public Result findAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> responseDtos = new ArrayList<>();
        for (Member member : members) {
            MemberResponseDto memberResponseDto = new MemberResponseDto(member.getId());
            responseDtos.add(memberResponseDto);
        }
        return new Result(responseDtos);
    }
}
