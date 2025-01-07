package three.finalproject.domain.data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import three.finalproject.domain.member.Member;
import three.finalproject.domain.member.MemberRepository;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */

/*    @PostConstruct
    public void init() {

        Member member = new Member();
        member.setId("admin2");
        member.setPassword("admin!");
        member.setName("admin2");

        memberRepository.save(member);
    }*/

}
