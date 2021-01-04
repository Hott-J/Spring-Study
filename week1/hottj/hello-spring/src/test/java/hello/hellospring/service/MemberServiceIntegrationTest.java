package hello.hellospring.service;
import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@Transactional // clearStore 할 필요없음. 테스트 실행해서 데이터 넣고, 테스트 끝나면 롤백.
class MemberServiceIntegrationTest {
    @Autowired MemberService memberService; //테스트이므로 그냥 필드 주입 방식으로 한다. 생성자 방식 x
    @Autowired MemberRepository memberRepository;

    @Test
    //@Commit
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("spring100"); //이미 DB에 있는 경우면 오류가 난다.
        //When
        Long saveId = memberService.join(member);
        //Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }
    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring1");
        Member member2 = new Member();
        member2.setName("spring1");
        //When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
//@SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
//@Transactional