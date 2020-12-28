## :cherry_blossom: Spring 기초

### 1. Spring 환경 설정
* Java 11, IntelliJ 설치

### 2. Spring 프로젝트 설정
* [https://start.spring.io](https://start.spring.io/) 사이트 접속하여 프로젝트 생성   
* 프로젝트 옵션
  * Project : Gradle Project
  * Language : Java
  * Sprint Boot : 2.4.1
  * Project Metadata
    * Group : hello
    * Artifact : hello-spring
  * Dependencies : Spring Web, Thymeleaf(html 만들어주는 템플릿 엔진)

### 3. 라이브러리 의존성
* spring-boot-starter-web
  * spring-boot-starter-tomcat : 톰캣(웹서버)
  * spring-webmvc : 스프링 웹 MVC
* spring-boot-starter-thymeleaf : 타임리프 템플릿 엔진(View)
* spring-boot-saarter(공통) : 스프링 부트 + 스프링 코어 + 로깅
  * spring-boot
    * spring-core
  * spring-boot-starter-logging
    * logback, slf4j
* spring-boot-starter-test
  * junit : 테스트 프레임워크
  * spring-test : 스프링 통합 테스트 지원
  * mockito
  * assertj

### 4. View 환경설정
* Welcome Page 기능
  * resources/static/에 index.html 파일을 올려두면 스프링 부트에서 Welcome page 기능을 제공한다.
* Thymeleaf 템플릿 엔진 사용
<img src="https://user-images.githubusercontent.com/61045469/103153029-ad3a9780-47d0-11eb-8181-14e4f38ca73d.PNG" width="90%" height="70%"></img><br/>
  * GetMapping annotation : HTTP GET 요청을 처리하는 method (HTTP POST 요청 처리는 PostMapping annotation)
  * return값 : Controller에서 return값으로 문자를 반환하면 viewResolver가 resources/templates 내에 있는 html파일명과 return값을 매칭시켜서 처리한다.
  
### 5. 빌드하고 실행
* 실행 환경 : Windows
* 콘솔로 이동하여 명령어 입력
  * gradlew build
  * cd build/libs
  * java -jar hello-spring-0.0.1-SNAPSHOT.jar
  * 실행 확인 (다시 build하고 싶을 경우 : gradlew clean build)

### 6. 스프링 웹 개발 기초
* 정적 컨텐츠   
<img src="https://user-images.githubusercontent.com/61045469/103153554-ba598580-47d4-11eb-85ff-448b981087dd.PNG" width="90%" height="70%"></img><br/>
  * 정적 컨텐츠는 파일 그대로 반환된다.
  <br/>
* MVC와 템플릿 엔진  
<img src="https://user-images.githubusercontent.com/61045469/103170901-462de900-488b-11eb-89c0-e2cd73d859e1.PNG" width="90%" height="70%"></img><br/>
  * model -> name은 key, spring은 value
  * 정적 컨텐츠와는 달리 Thymeleaf 템플릿 엔진이 html 파일을 rendering해서 반환한다.
  * 템플릿 엔진을 MVC 각각으로 쪼개서 rendering된 파일을 반환하는 방식이다.
  * MVC : Model, View, Controller (View : 화면을 그리는 역할, Model Controller : 비즈니스 로직, server backend 처리 담당)
  * Thymeleaf 장점 : server없이 파일을 열어서 볼 수 있다.
  <br/>
* API   
<img src="https://user-images.githubusercontent.com/61045469/103171584-c0ad3780-4890-11eb-984f-4764e679ad64.PNG" width="90%" height="70%"></img><br/>
  * getter, setter : 자바 빈 표준 방식 (단축키 : Alt + Insert)
  * ResponseBody annotation을 발견하면 viewResolver에게 요청하지 않는다.
  * viewResolver 대신 HttpMessageConverter가 동작
    * 문자열일 경우 : 문자열 데이터를 HTTP응답으로 넣어서 그대로 반환한다. -> String Converter가 동작
    * 객체일 경우 : 객체를 json 방식으로 데이터를 만들어서 HTTP응답으로 넣어 반환한다. -> Json Converter가 동작
    * 그 외 여러 포멧에 대한 HTTPMessageConverter도 등록되어 있다.
    * 보통은 객체 반환을 위주로 사용된다.
<br/>

## :cherry_blossom: 회원 관리 예제

### 1. 비즈니스 요구사항 정리
* 데이터 : 회원ID(시스템에서 설정), 이름(회원이 설정)
* 기능 : 회원 등록, 조회
* 아직 데이터 저장소가 선정되지 않음   
* 일반적인 웹 어플리케이션 계층 구조
<img src="https://user-images.githubusercontent.com/61045469/103172280-bb9eb700-4895-11eb-93ef-d351d009ea91.PNG" width="90%" height="70%"></img><br/>
  * 컨트롤러 : 웹 MVC의 컨트롤러 역할
  * 서비스 : 핵심 비즈니스 로직 구현(ex-회원은 중복 가입이 안됨)
  * 도메인 : 비즈니스 도메인 객체 (ex-회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨)
  * 리포지토리 : 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
  <br/>
* 클래스 의존관계   
<img src="https://user-images.githubusercontent.com/61045469/103172424-a70eee80-4896-11eb-8ff1-fa2fb3e13b09.PNG" width="70%" height="70%"></img><br/>
  * 회원 비즈니스 로직 : MemberService, 회원 리포지토리 : interface인 MemberRepository
  * 아직 데이터 저장소가 선정되지 않아 우선 interface로 구현 클래스를 변경할 수 있도록 설계
  * 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정
  * 개발을 진행하기 위해 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소(Memory MemberRepository) 사용

### 2. 회원 도메인과 리포지토리 만들기
* 회원 객체
```java
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
* 회원 리포지토리 인터페이스
```java
public interface MemberRepository {
    Member save(Member member); // 회원을 저장소에 저장
    Optional<Member> findById(Long id); // 저장소에서 id로 회원 찾아옴
    Optional<Member> findByName(String name); // 저장소에서 name 으로 회원 찾아옴
    List<Member> findAll(); // 저장소에서 지금까지 저장된 모든 회원 리스트 반환
}
```
* 회원 리포지토리 메모리 구현체
```java
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null 일 경우에도 리턴 가능
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name)) // 같은 경우에만 필터링됨
                .findAny(); // 찾은 경우 반환
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
    
    // Test 할 때 data clear 하기 위함
    public void clearStore() {
        store.clear();
    }
}
```

### 3. 회원 리포지토리 테스트 케이스 작성
* JUnit 프레임워크를 사용하여 테스트 케이스 작성 - 클래스 전체 한번에 테스트 가능
* 테스트 케이스를 통과하지 않으면 다음 단계로 못넘어가게 막는다.
* 어떤 method가 먼저 테스트가 될지는 모른다. 순서 상관 없이 method별로 따로 돌게 설계해야 한다.
* 따라서 테스트 하나 끝나고 나면 data clear 해줘야 한다. (@AfterEach)
* shift + F6 : 변수명 고치기
```
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // Test 실행 순서 의존관계를 없앤다. -> 하나의 테스트가 끝날 때마다 저장소를 비워준다.
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        Member result = repository.findById(member.getId()).get(); // optional 에서 값을 꺼낼 때

        // 검증 단계 - result, member 가 똑같은지 확인(인자 : 기대하는 값(Expected), 실제 값(Actual))
        // Assertions.assertEquals(member, result);

        // 다른 방법(더 편한 방법) - member 가 result 와 같은가?
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1); // 검증
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2); // 검증
    }
}
```
