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
import java.util.stream.Collectors;

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
    /**
     * 위 코드와 기능적으로 동일하나 stream을 잘 못써서 이렇게 했음
     * List<MemberResponseDto> collect = members.stream()
     *                 .map(member -> new MemberResponseDto(member.getId()))
     *                 .collect(Collectors.toList());
     *         return new Result(collect);
     *         마지막이 좀 어려웠는데 Result는 래퍼 클래스로 모든걸 받을 수 있음
     *         Result는 T를 타입으로하는 필드가 있는데 생성하면서 그 필드를 List<dto> 형태로 넣은 것이다! 좋은데 어려운 코드네요
     */
    }
}
