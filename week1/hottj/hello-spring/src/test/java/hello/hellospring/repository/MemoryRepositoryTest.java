package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryRepositoryTest {

    MemoryMemberRespository repository = new MemoryMemberRespository();

    @AfterEach //메소드 끝나고 데이터 클린해야함. 테스트 순서에 대한 의존성이 있으면 안되므로.
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member=new Member();
        member.setName("spring");
        repository.save(member);
        Member result=repository.findById(member.getId()).get(); //Optional 은 get으로 꺼내옴
        //System.out.println("Result="+(result==member));
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result=repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result=repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
