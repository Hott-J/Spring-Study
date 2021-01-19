
# :cherries: 회원 관리 예제 - 웹 MVC 개발

## :tulip: 홈 화면 추가
- 맨 처음 홈페이지에 들어왔을 때의 메인 화면을 담당

### 홈 컨트롤러 추가

```java
package hello.hellospring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
 @GetMapping("/")
 public String home() {
 return "home";
 }
}
```


### html 파일 추가
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
 <div>
 <h1>Hello Spring</h1>
 <p>회원 기능</p>
 <p>
 <a href="/members/new">회원 가입</a>
 <a href="/members">회원 목록</a>
 </p>
 </div>
</div> <!-- /container -->
</body>
</html>
```

#### :smile: 만약 @GetMapping("/")된 값이 없으면 index.html이 먼저 실행된다. 하지만 있다면 index.html은 우선순위에서 밀림!

## :tulip: 등록 기능 추가

- form이라는 형식으로 html에서 값을 받아옴
### 웹 등록 화면에서 데이터를 전달 받을 폼 객체
```java
package hello.hellospring.controller;
public class MemberForm {
 private String name;
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
}
```

### 회원 컨트롤러에서 회원을 실제 등록하는 기능
```java
@PostMapping(value = "/members/new")
public String create(MemberForm form) {
 Member member = new Member();
 member.setName(form.getName());
 memberService.join(member);
 return "redirect:/";
}
```

## :tulip: 조회 기능 추가

### 회원 컨트롤러에서 조회 기능
```java
@GetMapping(value = "/members")
public String list(Model model) {
 List<Member> members = memberService.findMembers();
 model.addAttribute("members", members);
 return "members/memberList";
}
```
### 회원 리스트 HTML
```<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
 <div>
 <table>
 <thead>
 <tr>
 <th>#</th>
 <th>이름</th>
 </tr>
 </thead>
 <tbody>
 <tr th:each="member : ${members}">
 <td th:text="${member.id}"></td>
 <td th:text="${member.name}"></td>
 </tr>
 </tbody>
 </table>
 </div>
</div> <!-- /container -->
</body>
</html>
```

# :cherries: 데이터베이스 연동 - JPA

## :tulip: JPA
- 자바 진영의 표준 인터페이스
- 본 프로젝트에서는 jpa의 hibernate 만 사용할 예정
- 객체와 ORM의로 구성됨

### jpa entity 코딩
```java
package hello.hellospring.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Member {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String name;
 public Long getId() {
 return id;
 }
 public void setId(Long id) {
 this.id = id;
 }
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
}
```

### 회원 리포지토리 설정
```java
package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
public class JpaMemberRepository implements MemberRepository {
 private final EntityManager em;
 public JpaMemberRepository(EntityManager em) {
 this.em = em;
 }
 public Member save(Member member) {
 em.persist(member);
 return member;
 }
 public Optional<Member> findById(Long id) {
 Member member = em.find(Member.class, id);
 return Optional.ofNullable(member);
 }
 public List<Member> findAll() {
 return em.createQuery("select m from Member m", Member.class)
 .getResultList();
 }
 public Optional<Member> findByName(String name) {
 List<Member> result = em.createQuery("select m from Member m where
m.name = :name", Member.class)
 .setParameter("name", name)
 .getResultList();
 return result.stream().findAny();
 }
}
```

### 스프링 설정 변경
- Jpa를 사용하려면 의존관계를 형성해주어야한다. 
```java
package hello.hellospring;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
@Configuration
public class SpringConfig {
 private final DataSource dataSource;
 private final EntityManager em;
 public SpringConfig(DataSource dataSource, EntityManager em) {
 this.dataSource = dataSource;
 this.em = em;
 }
 @Bean
 public MemberService memberService() {
 return new MemberService(memberRepository());
 }
 @Bean
 public MemberRepository memberRepository() {
// return new MemoryMemberRepository();
// return new JdbcMemberRepository(dataSource);
// return new JdbcTemplateMemberRepository(dataSource);
 return new JpaMemberRepository(em);
 }
}
```

## :tulip: 스프링 데이터 JPA
- 스프링 부트와 JPA만 사용해도 개발 생산성이 정말 많이 증가하고, 개발해야할 코드도 확연히 줄어듭니다.
여기에 스프링 데이터 JPA를 사용하면, 기존의 한계를 넘어 마치 마법처럼, 리포지토리에 구현 클래스 없이
인터페이스 만으로 개발을 완료할 수 있습니다. 그리고 반복 개발해온 기본 CRUD 기능도 스프링 데이터
JPA가 모두 제공합니다.

- JPA 회원 리포지토리
```java
package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
Long>, MemberRepository {
 Optional<Member> findByName(String name);
}
```

- 스프링 데이터 JPA를 사용할 수 있도록 설정
```
package hello.hellospring;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SpringConfig {
 private final MemberRepository memberRepository;
 public SpringConfig(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }
 @Bean
 public MemberService memberService() {
 return new MemberService(memberRepository);
 }
}
```


# :cherries: AOP

## AOP가 필요한 상황
- 모든 메소드의 호출 시간을 측정하고 싶다면?
- 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)

![AOP로직](https://user-images.githubusercontent.com/51367515/103616819-7acb3000-4f70-11eb-9b1a-07f57c842288.PNG)

## 적용
![스프링 컨테이너](https://user-images.githubusercontent.com/51367515/103617554-caf6c200-4f71-11eb-8b67-2b79081879bc.PNG)

- 시간 측정 AOP 등록
```java
package hello.hellospring.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class TimeTraceAop {
 @Around("execution(* hello.hellospring..*(..))")
 public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
 long start = System.currentTimeMillis();
 System.out.println("START: " + joinPoint.toString());
 try {
 return joinPoint.proceed();
 } finally {
 long finish = System.currentTimeMillis();
 long timeMs = finish - start;
 System.out.println("END: " + joinPoint.toString()+ " " + timeMs +
"ms");
 }
 }
}
```
![스프링 컨테이너](https://user-images.githubusercontent.com/51367515/103618090-b49d3600-4f72-11eb-94f6-235af1d8a509.PNG)

