package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, //extends로 상속, 인터페이스는 다중상속가능.
        Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);
}
