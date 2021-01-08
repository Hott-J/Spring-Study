package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRespository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRespository memberRespository;

    @BeforeEach
    public void beforeEach(){
        memberRespository = new MemoryMemberRespository();
        memberService=new MemberService(memberRespository);
    }
    @AfterEach
    public void afterEach(){
        memberRespository.clearStore();
    }

    @Test
    void 회원가입() {
        //given , 이게 주어지고
        Member member=new Member();
        member.setName("hello");

        //when , 이걸 실행했을 때
        Long saveId=memberService.join(member);

        //then , 이게 나와야한다
        Member findMember=memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1=new Member();
        member1.setName("spring");

        Member member2=new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));//member2 넣으면 예외가 터져야한다.
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        try{
//            memberService.join(member2);
//            fail();
//        }catch(IllegalStateException e) {
//            Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }
//        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}