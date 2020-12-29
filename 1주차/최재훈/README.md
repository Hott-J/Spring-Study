# Spirng boot 첫 프로젝트
----------------
### 1️⃣ 라이브러리 살펴보기
#### 🔶 스프링 부트 라이브러리
 * spring-boot-starter-web
    * spring-boot-starter-tomcat : 톰캣(웹서버)
    * spring-webmvc : 스프링 웹 MVC
 * spring-boot-starter-thymeleaf : 타임리프 템플릿 엔진(View)
 * spring-boot-starter(공통) : 스프링부트 + 스프링 코어 + 로깅
    * spring-boot
      * spring-core
    * spring-boot-starter-logging
      * logback, slf4j
### 2️⃣ 스프링 웹 개발 기초
#### 🔶 정적 컨텐츠
![image](https://user-images.githubusercontent.com/46257667/103171993-914bfa00-4893-11eb-9d31-047c98f15161.png)

✅ <동작과정><br>
웹 브라우저에서 url 요청 -> 내장 웹 서버 톰캣이 스프링에게 url 전달 -> hello-static과 맵핑된 controller가 없다 -> 스프링 부트가 resource에서 hello-static 찾아서 return

#### 🔶 MVC와 템플릿 엔진
Controller
```java
@Controller
public class HelloController{
   @GetMapping("hello-mvc")
   public String helloMVC(@RequestParam("name") String name, Model model) {
      model.addAttribute("name", name);
      return "hello-template";
   }
}
```
View
```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
   <p th:text="'hello '+ ${name}">hello! empty</p>
</body>
</html>  
```
![image](https://user-images.githubusercontent.com/46257667/103173536-0ec93780-489f-11eb-8257-4acf1a810869.png)

✅ <동작과정><br>
웹 브라우저에서 url 요청 -> 내장 웹 서버 톰캣이 스프링에게 url 전달 -> 스프링은 return값과 model을 viewResolver에게 전달 -> viewResolver가 thymeleaf 템플릿 엔진과 매핑 -> thymeleaf 템플릿 엔진이 넘어온 값들 처리 후 랜더링 -> view에 전달

#### 🔶 API
ResponseBody 문자 반환
```java
@Controller
public class HelloController {
   @GetMapping("hello-string")
   @ResponseBody
   public String helloString(@RequestParam("name") String name) {
      return "hello " + name;
 }
}
```
* ResponseBody 어노테이션 사용하면 viewResolver를 사용하지 않음
* HTTP body 부분에 문자 내용을 **직접 반환**
ResponseBody 객체 반환
```java
@Controller
public class HelloController {
   @GetMapping("hello-api")
   @ResponseBody
   public Hello helloApi(@RequestParam("name") String name) {
      Hello hello = new Hello();
      hello.setName(name);
      return hello;
   }
   static class Hello {
      private String name;
      public String getName() {
         return name;
      }
      public void setName(String name) {
         this.name = name;
      }
   }
}
```
* 객체를 반환할 때 **JSON**으로 변환된다.
* 스프링에서 default로 JSON 형식으로 변환.

![image](https://user-images.githubusercontent.com/46257667/103208494-1e4e8c00-4944-11eb-9976-48958ac72f77.png)

✅ <동작과정><br>
**@ResponseBody 어노테이션** -> **HTTPMessageConverter**가 작동 -> 문자, 객체로 알맞게 변환 -> 웹 브라우저로 return<br>
* 기본 문자처리 : StringHttpMessageConverter
* 기본 객체처리 : MappingJackson2HttpMessageConverter

### 3️⃣ 회원 관리 예제 - 백엔드 개발 🎯
- 비즈니스 요구사항 
- 회원 도메인과 리포지토리 만들기
- 회원 리포지토리 테스트 케이스 작성
- 회원 서비스 개발
- 회원 서비스 테스트
#### 🔶 비즈니스 요구사항 정리
❗️ 데이터 : 회원ID, 이름<br>
❗️ 기능 : 회원 등록, 조회<br>
❗️ 아직 데이터 저장소 정해지지 않음.

✅ <일반적인 웹 애플리케이션 계층 구조>

![image](https://user-images.githubusercontent.com/46257667/103216861-6d9fb700-495a-11eb-89fb-b5a251d6f820.png)
- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스 : 핵심 비즈니스 로직 구현
- 리포지토리 : 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인 : 비즈니스 도메인 객체, ex) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 

✅ <클래스 의존관계>

![image](https://user-images.githubusercontent.com/46257667/103217075-0df5db80-495b-11eb-8246-6a68a9637dd0.png)

- 아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계
- 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정
- 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 **메모리 기반의 데이터 저장소** 사용

#### ❓ In-Member Database (혹은 Main Memory database) ❓<br> 
🔵 장점
- 일반적인 디스크 기반 데이터베이스보다 접근 속도가 훨씬 빠르다.

🔴 단점
- 휘발성이다. DB 서버 전원이 꺼지면 데이터가 모두 날아갈 수 있다. 
- 데이터에 비해 RAM 용량이 적을 경우 가상메모리를 쓰게되어 성능저하의 원인이 된다.

#### 🔶 회원 도메인과 리포지토리 만들기

domain
```java
package hello.hellospring.domain;

public class Member {
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

Repository
```java
package hello.hellospring.repository;

public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public Member save(Member member) {
        member.setId(++sequence); // id값 세팅
        store.put(member.getId(), member); // store에 저장, map에 저장된다.
        return member;
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
```

#### 🔶 회원 리포지토리 테스트 케이스 작성
```java
package hello.hellospring.repository;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }
    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring1");
        repository.save(member2);
        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring1");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }


}

```

![image](https://user-images.githubusercontent.com/46257667/103224884-02f77700-496c-11eb-9dae-ecb58f1fba3e.png)

❌ ERROR 원인 ❌<br>
메소드를 순서 의존적으로 설계하였다. Test를 전체 실행하면 임의로 메소드가 실행되는데 findAll()에서 member1, member2를 이미 save했기 때문에 다른 메소드에서 동일한 객체를 save하게 되면 데이터가 충돌하게 된다. <br> 
⭕ 해결방법 ⭕ <br>
@AfterEach 어노테이션을 사용하여 콜백함수를 지정한 다음 하나의 테스트가 끝날 때마다 저장소나 공용 데이터들을 깔끔하게 지워줘야한다. 

#### 🔶 회원 서비스 개발
service
```java
package hello.hellospring.service;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    public Long join(Member member){
        //같은 이름이 있는 중복 회원 X
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member){
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
```
#### 🔶 회원 서비스 테스트

ctrl + shift + T -> 자동으로 테스트 코드 만들어 주는 cmd <br>

```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;// 다른 리포지토리 사용하는 중이어서 불안하다.
    @BeforeEach
    public void berforEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }
    @Test
    void join() {
        //given
        Member member = new Member();
        member.setName("hello");
        //when
        Long saveId = memberService.join(member);
        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void findMembers() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");
        //when

        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        memberService.join(member1);
//        try{
//            memberService.join(member2);
//            fail();
//        }catch(IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");
//        }
        //then
    }

    @Test
    void findOne() {
    }
}
```
### 4️⃣ 스프링 빈과 의존관계

Dependency injection




