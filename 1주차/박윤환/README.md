# 📖 스프링 프로젝트 환경설정

## Spring initailizer를 통한 시작

복잡한 과정 없이 간단하게 웹서버를 8080포트에 띄울 수 있다

## build.gradle

여러 의존성 파일들의 연결관계를 찾아서 끌어온다. 

## println 말고 log로 기록을 남기자 (로그 라이브러리)

- `slf4j`
- logback → log에 대해 알아보자

**Logging**은 시스템을 작동할 때 시스템의 작동 상태의 기록과 보존 이용자 습성 조사 및 시스템 동작 분석을 위해 작동중의 각종 정보를 기록해두는 것이다. (일련의 사건을 **시간에 따라** 기록하는 것)

- Log 라이브러리 사용 이유 (println X)
    - 에러/장애 발생 시 추적할 수 있는 정보 (ex. 날짜, 시간, 생성자, 타입)
    - 로그 내용을 서버나 다른 곳으로 가져올 수 있다.

## Thymeleaf

템플릿 엔진은 [ 템플릿 양식 + 데이터모델에 따른 입력 자료 ]를 합성해 결과 문서를 출력하는 Software.

- 웹 템플릿엔진 = 웹 템플릿 + 웹 컨텐츠 정보
- 서버 사이드 자바 템플릿 엔진 : 서버 or DB에서 가져온 데이터를 Template에 넣어 html을 그려 클라이언트에 전달함

# 📖 스프링 웹 개발 기초 & 백엔드 개발

- **정적 컨텐츠**
    - resources 디렉토리의 **/static 디렉토리**에 위치함
    - 항상 스프링 컨테이너의 컨트롤러가 우선순위를 가지며 없을 때만  정적 컨텐츠 찾음

- **MVC와 템플릿 엔진**
    - **MVC** (Model, View, Controller)
        - **viewResolver** : 뷔를 찾아주고 템플릿에 연결시켜줌

- **API**
    - `@ResponseBody`를 사용해 viewResolver를 사용하지 않음
    - HTTP Body에 직접 내용을 반환
        - viewResolver 대신 HttpMessageConverter가 사용됨
            - 이를 통해 객체라면 JsonConverter를 통해 Json 형태로 변환

## 백엔드 개발

데이터 :  회원ID, 이름
기능    :  회원 등록, 조회

- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스    : 핵심 비즈니스 로직 구현
- 리포지토리 : 데이터베이스 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인     : 비즈니스 도메인 객체

현재는 데이터 저장소 **X**, 우선 **인터페이스**로 **구현 클래스**를 변경할 수 있도록 설계 

구현체로는 가벼운 **메모리 기반의 저장소**를 사용 (강의 코드)

## JUnit 프레임워크를 통한 테스트

- main 메서드나 컨트롤러를 통해 해당 기능을 실행하지 않고 **간단히 테스트를 실행**

    - `@AfterEach` : 메모리 DB에 직전 테스트의 결과가 남을 수 있다. 이렇게 되면 다음 이전 테스트 때문에 다음 테스트가 **실패할 가능성이 있다.** **@AfterEach 를 사용하면 각 테스트가 종료될 때 마다 이 기능을 실행한다.**
    - 테스트는 각각 **독립적**으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.
    
# 회원 서비스 개발 및 테스트

## **서비스** 구현

- 서비스는 `join()`, `findMembers()`, `findOne()` 같은 메서드로
좀 더 **사용자 친화적**으로 만들어진다.

- **DI (Dependency Injection)**

```java
public class MemberService {
private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
			this.memberRepository = memberRepository;
	}
...
}
```

- 생성자를 이용해 회원리포지토리를 사용하는 곳에서 넣어줄 수 있도록 한다. (생성자 DI)

- `@BeforeEach` 를 통해 test 실행전에 memberRepository를 할당 할 수 있다.

### Test는 형식에 맞춰 작성해보자

- given : 주어진 상황
- when : 실행
- then   : 실행 후 결과

### 단축키

- cmd + option + v // ctrl + t // shift + f6 등의 단축키 활용 (맥북용)

# 스프링 빈과 의존관계 & 웹 MVC 개발

**멤버 컨트롤러 - 멤버 서비스
(컨트롤러가 멤버 서비스를 의존한다)**

- 시작 패키지 하위 → `스프링 빈`에 등록하기
- 회원은 공유되는 자원 → 서로 의존 관계를 설정

1. **컴포넌트 스캔**과 **자동 의존관계** 설정
    - `@controller` 와 `@Autowired` 와 같은 Annotation을 통해 스프링이 연관된 객체를 스프링 컨테이너에 스프링 빈에 등록
    - DI에는 `필드 주입`, `setter 주입`, `생성자 주입` 이렇게 3가지 방법

 2. 자바 코드로 직접 스프링 빈 등록

- 따로 클래스 파일을 만들어 `@Configuration` 을 이용하면 직접 등록할 수 있다.
- 향후 리포지토리를 다른 리포지토리로 변경할 예정일 때 **`매우 유용`**하게 사용할 수 있다.
`ex) 메모리데이테베이스 -> 실 데이터베이스`

## 웹 MVC 개발

- 홈 컨트롤러 클래스 파일을 만들어 index.html보다 앞서 참조 될 home을 설정한다.
- `GetMapping` 은 url, return은 참조할 html이라고 볼 수 있다. (현재까지는 html파일의 경로를 반환했으므로..)
- `postMapping` 을 이용해 **form형태** 내용을 전달 받을 수 있다.
- **thymeleaf** 문법을 통해 모든 객체를 웹 화면에 뿌려줄 수 있다. (ex. th:each)
