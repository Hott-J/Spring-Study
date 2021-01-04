## :cherry_blossom: 회원 관리 예제 - 웹 MVC 개발

### 1. 회원 웹 기능 - 홈 화면 추가
* HomeController 추가
```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";  // templates 폴더 내에 있는 home.html 을 찾는다.
    }
}
```
* static/index.html(Spring boot가 제공하는 Welcome page 기능)과 templates/home.html("/" Mapping) 중 무엇이 더 우선순위가 높을까?
  * 우선, HTTP 요청이 오면 Spring Container 내에 있는 관련 Controller가 있는지 찾는다. 없으면 그때 static file을 반환한다.
  * HTTP 요청으로 맨 처음 "/"가 오는데, "/"에 mapping되는 Controller가 있으면 templates 내에 있는 html 파일을 리턴한다. 이렇게 되면 static file은 무시된다.
  * @GetMapping("/")을 처리하는 Controller를 지우면 다시 static file이 등장하게 될 것이다.
  * 결국, Controller가 정적 파일보다 우선순위가 높다고 할 수 있다!

### 2. 회원 웹 기능 - 등록
* 회원 등록 폼 Controller
  * MemberController에 GetMapping("/members/new") 추가
  * templates폴더 밑에 members폴더 생성 후 createMembersForm.html 파일 생성
  ```java
  @Controller
  public class MemberController {

      private final MemberService memberService; // 하나만 만들어져 있으면 된다.

      @Autowired
      public MemberController(MemberService memberService) {
          this.memberService = memberService;
      }

      @GetMapping("/members/new")
      public String createForm() {
          return "members/createMembersForm";
      }
  }
  ```
  
* 웹 등록 화면에서 데이터를 전달 받을 폼 객체
  * templates/members/createMembersForm.html 에 있는 name="name"과 매칭된다.
  ```java
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
    
* 회원 Controller에서 회원을 실제 등록하는 기능
  * MemberController에 PostMapping("/members/new") 추가
  * PostMapping : form에 data를 넣어서 전달, 등록 / GetMapping : data 조회
  ```java
  @Controller
  public class MemberController {

      private final MemberService memberService; // 하나만 만들어져 있으면 된다.

      @Autowired
      public MemberController(MemberService memberService) {
          this.memberService = memberService;
      }

      @GetMapping("/members/new")
      public String createForm() {
          return "members/createMembersForm";
      }

      @PostMapping("/members/new")
      public String create(MemberForm form) {
          Member member = new Member();
          member.setName(form.getName());
          memberService.join(member);
          return "redirect:/";
      }
  }
  ```

### 3. 회원 웹 기능 - 조회
* 회원 Controller에서 회원을 조회하는 기능
  * MemberController에 GetMapping("/members") 추가
  * templates/members폴더 밑에 memberList.html 파일 생성
  ```java
  @Controller
    public class MemberController {

        private final MemberService memberService; // 하나만 만들어져 있으면 된다.

        @Autowired
        public MemberController(MemberService memberService) {
            this.memberService = memberService;
        }

        @GetMapping("/members/new")
        public String createForm() {
            return "members/createMembersForm";
        }

        @PostMapping("/members/new")
        public String create(MemberForm form) {
            Member member = new Member();
            member.setName(form.getName());

            memberService.join(member);

            return "redirect:/";
        }

        @GetMapping("/members")
        public String list(Model model) {
            List<Member> members = memberService.findMembers(); // 모든 회원 list가 들어감.
            model.addAttribute("members", members);
            return "members/memberList";
        }
    }
    ```
<br/>

## :cherry_blossom: Spring DB 접근 기술

### 1. H2 데이터베이스 설치
* 지금까지는 메모리에 저장했기 때문에, 서버가 꺼지면 모든 data가 날라간다.
* [https://www.h2database.com](https://www.h2database.com) 사이트 접속하여 Version 1.4.200 (2019-10-14) 다운로드 및 설치
* 권한 주기 : chmod 755 h2.sh
* 실행 : h2.bat(윈도우), ./h2.sh(맥, 리눅스)
* 데이터베이스 파일 생성 방법(JDBC URL)
  * jdbc:h2:~/test(최초 한번만)
  * ~/test.mv.db 파일 생성 확인하기
  * 이후부터는 jdbc:h2:tcp://localhost/~/test 로 접속. (파일로 접속할 경우 application과 웹 콘솔의 충돌이 일어날 수 있다.)
* 테이블 생성하기
  * 테이블 관리를 위해 프로젝트 루트에 sql/ddl.sql 파일을 생성
  ```
  drop table if exists member CASCADE;
  create table member
  (
    id bigint generated by default as identity,
    name varchar(255),
    primary key (id)
  );
  ```
  * 테이블 data 조회
  ```
  SELECT * FROM MEMBER
  ```
  * 테이블 data 추가(id는 generated by default as identity설정되어 있어서, 값 입력 안해도 자동으로 설정됨)
  * 이유는 모르겠지만 values("spring") 쌍따옴표 사용하면 에러가 나서 홑따옴표를 사용하였다.
  ```
  insert into member(name) values('spring')
  ```
  * 테이블 data 삭제
  ```
  delete from member
  ```
  
### 2. 순수 JDBC
* JdbcMemberRepository 추가한 후 SpringConfig 수정(실행할 때 H2 꼭 킨 상태여야 한다!)\
* Spring 특징   
<img src="https://user-images.githubusercontent.com/61045469/103458782-be465400-4d4e-11eb-9a66-a9c8c18f67ce.PNG" width="70%" height="50%"></img><br/>
  * 개방-폐쇄 원칙(OCP, Open-Closed Principle) : 확장에는 열려있고, 수정, 변경에는 닫혀있다.
  * 객체지향적인 설계 - 다형성
    * interface를 두고 구현체를 바꿔서 실행할 수 있다.
    * 스프링의 DI(Dependencies Injection)을 사용하면 기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경할 수 있다.
  * 데이터를 DB에 저장하므로 스프링 서버를 다시 실행해도 데이터가 안전하게 저장된다.

### 3. Spring 통합 테스트
* 테스트 코드는 편한 방식으로 만드는 편이다.
* @SpringBootTest 역할
  * 스프링 컨테이너와 테스트를 함께 실행한다. (실제로 스프링을 실행시키면서 테스트를 진행한다.)
* @Transactional 역할
  * @Transactional를 안쓰면 테스트 한 data가 DB에 저장되어 있다.
  * @Transactional를 쓰면 DB에 data commit 단계 전까지 수행한다. 결국 DB에 data insert query 실행 후, 테스트 끝나면 data를 다 지우게 된다. (DB에 반영X)
  * 따라서 다음 테스트를 반복적으로 수행할 수 있게 한다.
* @Commit을 사용하면 실제로 DB에 commit까지 수행한다.

### 4. Spring JdbcTemplate
* 스프링 JdbcTemplate과 MyBatis 같은 라이브러리는 JDBC API에서 본 반복 코드를 대부분 제거해준다. 하지만 **SQL은 직접 작성**해야 한다.

### 5. JPA(Java Persistence API)
* JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 자동으로 만들어서 실행해준다. (쿼리 작성할 필요X)
* JPA를 사용하면, SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환을 할 수 있다.
* JPA를 사용하면 개발 생산성을 크게 높일 수 있다. - Member 객체를 보고 table도 알아서 생성하게 할 수도 있다!
* JPA는 자바의 표준 인터페이스이며 구현(Hibernate)은 여러 업체들이 하는 것으로 볼 수 있다.
* JPA 설정
  * show-sql : JPA가 생성하는 SQL을 출력한다.
  * ddl-auto : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 none을 사용하면 해당 기능을 끈다. (none 대신 create를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성해준다.)
* @GeneratedValue(strategy = GenerationType.IDENTITY) : DB가 알아서 생성해 주는 것을 IDENTITY라고 한다.


### 6. Spring 데이터 JPA
* 스프링 데이터 JPA를 사용하면, 리포지토리에 구현 클래스 없이 **인터페이스**만으로도 개발을 할 수 있다.
* 기본 CRUD 기능도 스프링 데이터 JPA가 모두 제공한다. 
* 스프링 데이터 JPA 회원 리포지토리
  ```java
  // interface가 interface를 상속받을 때에는 inplements가 아닌 extends 사용
  // interface는 다중 상속이 가능하다.
  public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

      // JPA가 자동으로 select m from Member m where m.name = ? 쿼리를 만듦
      @Override
      Optional<Member> findByName(String name);
  }
  ```
* 스프링 데이터 JPA 회원 리포지토리를 사용하도록 SpringConfig 설정 변경
  * 스프링 데이터 JPA가 SpringDataJpaMemberRepository에 대한 구현체를 자동으로 만들고 스프링 빈으로 자동으로 등록까지 해준다.
  ```java
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
* 스프링 데이터 JPA 제공 기능
  * 인터페이스를 통한 기본적인 CRUD
  * findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공
  * 페이징 기능 자동 제공
<br/>

## :cherry_blossom: AOP(Aspect Oriented Programming) - 관점 지향 프로그래밍

### 1. AOP가 필요한 상황
* 모든 메소드의 호출 시간을 측정하고 싶다면?
* 회원 가입 시간, 회원 조회 시간을 측정하고 싶다면?
  ```java
  public long join(Member member) {
        long start = System.currentTimeMillis();

        try {
            validataduplicateMember(member);
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs +"ms");
        }
    }
  ```
* 문제
  * 회원가입, 회원 조회에 시간을 측정하는 기능은 핵심 관심 사항(핵심 비즈니스 로직)이 아니다.
  * 시간을 측정하는 로직은 공통 관심 사항이다.
  * 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다.
  * 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다.
  * 시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.
* 따라서 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern) 으로 분리해야 한다! -> **AOP**
  
### 2. AOP 적용
*  공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern) 분리
<img src="https://user-images.githubusercontent.com/61045469/103474507-f352b500-4de7-11eb-8edc-0082e266a52a.PNG" width="70%" height="50%"></img><br/>

* 시간 측정 AOP 등록
  ```java
  @Aspect
  @Component
  public class TimeTraceAop {

      @Around("execution(* hello.hellospring..*(..))") // 공통 관심사항을 어디에 적용할 것인가 - hellospring 패키지 하위에 다 적용
      public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
          long start = System.currentTimeMillis();
          System.out.println("START: " + joinPoint.toString());
          try {
              return joinPoint.proceed();
          } finally {
              long finish = System.currentTimeMillis();
              long timeMs = finish - start;
              System.out.println("END: " + joinPoint.toString()+ " " + timeMs + "ms");
          }
      }
  }
  ```
  * hellosping폴더 밑에 있는 service폴더에만 적용하고 싶으면 다음과 같이 설정하면 된다.
  ```java
  @Around("execution(* hello.hellospring.service..*(..))")
  ```
  
* 해결
  * 회원가입, 회원 조회등 핵심 관심사항과 시간을 측정하는 공통 관심 사항을 분리한다.
  * 시간을 측정하는 로직을 별도의 공통 로직으로 만들었다.
  * 핵심 관심 사항을 깔끔하게 유지할 수 있다.
  * 변경이 필요하면 이 로직만 변경하면 된다.
  * 원하는 적용 대상을 선택할 수 있다.
  
* AOP 동작 원리
  * AOP 적용 전 의존관계   
  <img src="https://user-images.githubusercontent.com/61045469/103477383-c8755a80-4e01-11eb-92ea-62d1463cf33a.PNG" width="70%" height="50%"></img><br/>
  * AOP 적용 후 의존관계   
  <img src="https://user-images.githubusercontent.com/61045469/103477384-ca3f1e00-4e01-11eb-917b-d0ef1ffdca44.PNG" width="70%" height="50%"></img><br/>
    * 가짜(프록시) helloController, memberService, memberRepository를 만들어낸다.
    * memberController는 진짜 memberService가 아닌 가짜 memberService를 호출한다.