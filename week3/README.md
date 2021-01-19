## :book: 강의 질문 내용 정리

### :one: Week1 & Week2

### :smile: Spring 사용 이유?

- 자바는 복잡성을 해결할 수 있고 탄탄한 오픈소스가 많다.
- 대용량 트래픽이나 성능 부분에서 자바가 가진 이점
- 엔터프라이즈 환경에서 검증된 기술들이 많이 있다.

### :smile: 웹 애플리케이션 개발의 랜더링 방식

- **서버 사이드 렌더링**
  - 서버에서 완전한 HTML을 만들어서 내려줌.
    - jsp, thymeleaf, velocity, freemarker 등
  - 장점: 단순하고, 학습 곡선이 낮다. 백엔드 개발자도 쉽게 개발할 수 있다.
  - 단점: 동적이면서 복잡한 화면을 만들기 어렵다.

- **클라이언트 사이드 렌더링**
  - 서버는 API만 제공하고, 자바스크립트 프레임워크가 템플릿과 서버 API 응답 결과를 조합해서 HTML 화면을 동적으로 만듦.
    - react, vue.js, angularJS 등
  - 장점: 동적이고, 복잡한 화면을 만들기 좋다.
  - 단점: 공부할 분량이 매우 많다. 자바스크립트에 능숙해야 한다. 웹 프론트엔드 개발자라는 전문 분야가 있다.

- *선택지*
  - 자바스크립트에 자신있고, 완전 풀스택으로 갈것이다. = react + typescript
  - **백엔드 개발이 좋고, 스프링이 좋은데, 어쩔 수 없을 때 화면을 찍겠다. = thymeleaf**
  - 레거시 하는 회사에 입사해야 한다. = jsp (비추)
  - velocity는 그냥 그걸 사용하는 회사에 입사하고 고민해도 됩니다. (쉽다)
  
### :smile: 타임리프의 장점

`<p th:text="'안녕하세요'" + ${data}" > 안녕하세요. 손님</p>`
-  *안녕하세요. 손님 부분은 기본 값* th:text 부분이 처리될 수 있는 서버 환경에서는 th:text의 값으로 기본 값의 내용을 변경해버린다.
- *서버가 없는 환경에서는* 해당 html파일을 웹 브라우저에서 파일로 바로 열어보면 **안녕하세요. 손님이 출력**. 서버가 없는 환경이기 때문에 ht:text 부분이 동작하지 않는다. 이때는 순수한 html을 그대로 열어본 것 처럼 기본 값이 출력. 서버 없이 html 파일만 열었을 때도 기본값을 통해 결과를 볼 수 있는 장점이 있다.
- *JSP 같은 경우에는* JSP 파일을 서버 없이 단순히 파일로만 열어보면 그 안에 프로그래밍 로직이 들어가기 때문에 **html이 다 깨져보입니다.** *thymeleaf는 이런 메커니즘으로 html파일을 열어 볼 수 있는 장점이 있습니다.*

### :smile: @RequestParam

- **@RequestParam을 쓰지 않아도 쿼리스트링으로 받은 값을 사용할 수 있다.**
  - 쿼리스트링 : ` http://localhost:8080/t~?name=asdf`,   `public String helloMvc(String name)`  이 경우 **String name에 asdf이 전달됨.**
  - *단, 쿼리스트링의 key값이 String name과 다를 경우(name이 아닐 경우) String name에는 null이 전달됨.*
  - ` http://localhost:8080/~?nme=asdf`,  `public String helloMvc(String name)` -> String name == null

- @RequestParam 뒤에 오는 String name은 컴파일 이후 변수명이 변경되어 혼동을 초래할 수 있음. 이를 방지하기 위해 *@RequestParam("name")* 으로 **쿼리스트링의 key값은 name이라는 것을 명확하게 표현.**
  - @RequestParam("name")을 사용했을 경우 쿼리스트링에서는 반드시 ` http://localhost:8080/~?name=asdf`으로 사용하여야 함. 만약 @RequestParam("name") String nme 이 상황에서 ` http://localhost:8080/~?nme=asdf`으로 받으면 에러 발생. **RequstParam을 썼으면 반드시 RequestParam과 key값을 맞춰야 함.**
  
  - RequestParam없이 그냥 String name만 했을 때는 이름을 다르게 받으면 그냥 null값이 전달되었으나 RequestParam을 사용했을때 이름을 다르게 받으면 null전달이 아니라 에러가 뜨는 이유?
    - RequestParam은 key값을 명확하게 하기 위한 용도로 명시된 key값을 반드시 사용해야만 한다는 강제성을 부여. 반드시 사용해야하는 key값을 사용하지 않았기 때문에 null전달이 아니라 에러 발생한 것. (@RequestParam의 required 변수의 기본값이 true)

- @RequestParam 안에 다른 매개변수들이 들어올 수도 있음. 
  - @RequestParam(name, defaultValue, required, value)
  - name : 강의에서 사용한 파라미터
  - defaultValue : name이 전달되지 않을 경우 전달해줄 기본값
  - required : name 값의 강제성 여부 (디폴트는 true)
  - value : name과 같은 역할을 하는 name의 alias. 단, name과 value를 동시에 쓸 수 없음. 

- @RequestParam은 클라이언트가 보낸 HTTP 요청 쿼리 정보를 서버에서 받을 수 있는 방법입니다.
  - @RequestParam("name")이라고 되어 있으면 `http://localhost:8080/~?name=` 여기에서 name을 매칭해서 값을 읽어옵니다.
  - 추가로 @RequestParam(name ="name")이라고 해도 되고, 애노테이션에 파라미터가 하나만 있으면 다음과 같이 줄일 수 있습니다. @RequestParam("name")
  
### :smile:  domain은 entity + 2번째 의미의 vo들의 모음

- **VO**
  - 단순히 데이터 값을 전달하기 위한 용도로 사용되는 객체라는 뜻 (DTO와 같은 뜻으로 혼용해서 사용. DTO = 데이터를 전송하는 목적으로 사용하는 객체)
  - 도메인 주도 설계에서 이야기하는 값 객체(Value Object)의 의미.
- **repository := dao (비슷함)**
  - 이 둘은 거의 같다고 생각하셔도 무방. 좀 더 깊이있게 차이를 설명하면, repository는 엔티티 객체를 보관하고 관리하는 저장소이고, dao는 데이터에 접근하도록 DB접근 관련 로직을 모아둔 객체. 둘다 개념의 차이일뿐 실제로 개발할 때는 비슷하게 사용
  
### :smile: 다형성

자바는 객체 지향 언어이고, 객체 지향 언어의 꽃은 바로 *다형성(Polymorphism)* 에 있다.
여기서는 단순하게 `Map store = new HashMap()` 이렇게 선언.
이 부분만 보면 HashMap store = new HashMap()와 비교해서, 이점은 다음과 같다.

- Map 인터페이스의 제약을 따르겠다는 의도를 명확하게 드러냄.
- 사용하는 코드가 Map 인터페이스 제약을 따르기 때문에 향후 변경시에 사용코드를 변경하지 않아도 됨.
- HashMap을 다른 클래스로 변경이 필요하면 선언하는 코드만 변경. 사용하는 코드를 고민하지 않아도 됨.
- 다른 개발자들이 이 코드를 나중에 더 성능이 좋거나 동시성 처리가 가능한 종류의 구체적인 Map으로 변경해야 할 때 HashMap store = new HashMap()이라고 되어 있다면, 변경 시점에 상당히 많은 고민을 해야 하지만 Map store = new HashMap()으로 선언이 되어 있다면 편안하게 선언부를 변경.
- 개발은 무의미한 자유도를 제공하는 것 보다, 제약을 부여하는 것이 혼란을 줄이고, 유지보수하기 쉽다.
- 만약 정말 HashMap의 구체적인 기능을 사용해야 한다면 HashMap store = new HashMap() 이라고 선언하는 것이 맞다.

- 여기서 한발 더 나가면 다음과 같은 설계도 가능.

```java
class MemberRepository {
  private Map store;
  public MemberRepository(Map store) {
    this.store =store
  }
}
```
- 외부에서 선언하는 코드

```java
new MemberRepository(new HashMap());
new MemberRepository(new ConcurrentHashMap());
```
- **MemberRepository를 전혀 변경하지 않고, 외부에서 구현 객체를 생성해서 파라미터로 넘길 수 있. 이런 것을 의존관계 주입( DI )이라고 하는데요. 이런 방법도 다형성 덕분에 사용.**

### :smile: 필드 주입 vs 생성자 주입

- 필드 주입시 MemberService를 바꿀 수 없다는 의미는, 이미 주입된 객체를 실행 중간에 바꾼다는 의미라기 보다는 다음과 같은 의미가 있다.
테스트 실행시 스프링 컨테이너의 도움 없이 MemberService가 가지고 있는 여러 Repository를 *자유롭게 변경하면서 테스트 할 수 있어야 한다.* **그런데 필드 주입을 사용하면, 스프링 컨테이너가 없을 때 의존하는 객체를 변경할 수 있는 방법이 없습니다.**
- 반면에 *생성자 주입에서는* 스프링 컨테이너의 도움 없이 직접 `new MemberService(new XxxRepository)`와 같은 식으로 **스프링 컨테이너의 도움 없이 원하는 객체를 변경해서 테스트 하거나 실행할 수 있습니다.**

### :smile: 포워딩 vs 리다이렉트

- **포워딩** 
  - 서버 내부에서 일어나는 일
  
1. 웹 브라우저 URL 창에 /event을 입력하고 엔터
2. 서버가 /event URL을 전달 받음
3. 서버가 서버 내부에서 /event -> /new-event로 포워딩
4. /new-event 결과를 내부에서 렌더링
5. 클라이언트에게 렌더링 된 결과를 반환
6. 웹 브라우저에 응답 결과가 보이고 URL 창은 처음 입력한 /event로 유지됨

- **리다이렉트**
  - 웹 브라우저가 인식하고 URL 경로를 실제 변경.

1. 웹 브라우저 URL 창에 /event을 입력하고 엔터
2. 서버가 /event URL을 전달 받음
3. 서버가 /event -> /new-event로 리다이렉트
4. 서버는 /new-event로 리다이렉트 하라는 결과를 웹 브라우저에 반환
5. 웹 브라우저는 URL 창에 자동으로 /new-event를 입력하고 엔터(자동으로 일어나고, 실제 URL 창에 입력 결과가 /new-event로 변경됨)
6. 서버가 /new-event URL을 전달 받음
7. /new-event 결과를 내부에서 렌더링
8. 클라이언트에게 렌더링 된 결과를 반환
9. 웹 브라우저에 응답 결과가 보이고 URL 창은 리다이렉트 된 /new-event로 유지됨

- **post/redirect/get 방법**
  - 웹 브라우저에서 POST로 글쓰기를 요청했는데, 그러면 서버에 글이 써지겠죠? 그러면 문제는 웹 브라우저를 그 상태로 새로고침 하면 POST로 글쓰기가 또 호출됨! 그러면 글이 중복해서 또 써지겠죠? 이게 만약 쇼핑몰이라면 중복 주문 될 수 도 있다.(물론 그렇지 않게 잘 대비.) 그래서 post의 결과를 get으로 조회 화면으로 리다이렉트 해버리는 것. 그러면 사용자가 실수로 새로고침을 눌러도 조회 화면만 다시 호출하게 됨.

### :smile: AOP 순환참조

- 직접 @Bean으로 등록했을 때 순환참조가 발생하는 이유는 다음과 같다.
```java
@Configuration
public class SpringConfig {

    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
}

@Aspect
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))") // !!!!!!!!!!!!!!!!!!!!문제점!!!!!!!!!!!!!!!!!!!!
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { }
}
```
- TimeTraceAop의 AOP 대상을 지정하는 @Around 코드를 보면, **SpringConfig의 timeTraceAop() 메서드도 AOP로 처리.** 그런데 이게 바로 자기 자신인 TimeTraceAop를 생성하는 코드. 그래서 순환참조 문제가 발생.
- *반면에 컴포넌트 스캔을 사용할 때는* AOP의 대상이 되는 이런 코드 자체가 없기 때문에 문제가 발생하지 않았습니다.
그러면 AOP 설정 클래스를 빈으로 직접 등록할 때는 어떻게 문제를 해결하면 될까? 바로 다음과 같이 AOP 대상에서 SpringConfig를 빼주면 된다.

```java
@Aspect
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..)) && !target(hello.hellospring.SpringConfig)") // 순환참조 핸들링
    //@Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {...}
}
```

### :smile: AOP vs 인터셉터

- 사실 AOP나 Intercepter둘다 공통 관심사를 해결하기 위한 목적을 가지고 있다. 인터셉터는 웹과 관련된 기능을 해결하는 것에 특화되어 있다. 특히 HttpRequest 관련된 정보를 받아서 해결할 수 있다. 그리고 최근 컨트롤러를 설계할 때 파라미터로 HttpRequest를 잘 안쓰기 때문에, 컨트롤러를 AOP로 처리하면 이런 부분을 받아서 해결하기가 참 어렵다. 반면에 인터셉터는 HttpRequest 정보를 그냥 넘겨주기 때문에, 편리. Interceptor는 웹에 특화되어 있기 때문에 웹과 관련된 기술을 사용하는 포인트에서만 사용할 수 있어서 범용성이 없다. 반면에 AOP는 범용성이 어마어마 하다. 실무에서 가장 많이 사용하는 AOP의 대표적인 사례는 @Transactional을 활용한 트랜잭션 관리, @Async를 통한 비동기 처리 등이 있다.

### :two: Week3

### :smile: Bean 등록

```java
@Bean
public String keesun() {
  return "keesun";
}
```

```java
@Autowired
String keesun;
```

- 등록해서 사용시 @Autowired를 사용한다.
- String이라틑 타입의 빈을 꺼내 쓴 것. 컨테이너 안에 들어있는 빈을 꺼내올 때 @Autowired를 사용하면 꺼내오려는 빈의 타입을 보고 꺼내려고 시도함.
- 빈을 등록할 때 사용한 함수 이름이 빈의 이름이 되고, 그 빈의 이름으로 꺼내 올 수도 있다. 하지만 여기서는 타입으로 꺼내온 것이며, keesun 대신 다른 이름을 써도 가져올 수 있을 것

#### :book: URI와 URL의 차이점
<img src="https://user-images.githubusercontent.com/61045469/104922630-4e86c900-59de-11eb-84b9-6e7d01add6b3.png" width="30%" height="10%"></img><br/>
* URI(Uniform Resource Identifier)
  * resource 식별자
  * resource의 식별은 resource의 위치(URL)를 표시하거나 고유한 이름(URN)으로 접근할 수 있다.

* URL(Uniform Resource Locator)
  * resource를 access할 수 있는 위치를 나타낸다.
  * ex) http://naver.com - 네이버 사이트의 URL(주소)
  
* URN(Unifrom Resource Name)
  * resource의 name
  * resource의 위치에 영향을 받지 않는다.
  * ex) ISBN 0-486-27557-4
  
* 통상적으로 모든 URL을 URI로 인정한다.

# :book: Servlet과 Servlet Container 
>클라이언트가 어떠한 요청을 하면 그에 대한 결과를 다시 전송해주어야 하는데, 이러한 역할을 하는 자바 프로그램.

서블릿이란 자바를 사용하여 웹을 만들기 위해 필요한 기술. 예를 들어, 어떠한 사용자가 로그인을 하려고 할 때. 사용자는 아이디와 비밀번호를 입력하고, 로그인 버튼을 누른다. 그때 서버는 클라이언트의 아이디와 비밀번호를 확인하고, 다음 페이지를 띄워주어야 하는데, 이러한 역할을 수행하는 것이 바로 서블릿(Servlet). 그래서 서블릿은 자바로 구현 된 *CGI라고 흔히 말한다.

## Spring MVC 구성요소들 

![image](https://user-images.githubusercontent.com/46257667/105039729-41341200-5aa4-11eb-8218-c6d9a9e0976f.png)

```알아낸 것들```

* Web Server vs Servlet Container vs WAS(web application server)

**web server** : 웹 서버로 들어온 요청을 받아 컨테이너로 전송하고 웹 컨테이너의 결과값을 받아 클라이언트에게 전송한다. 정적인 리소스를 처리한다. <br>
**servlet conatiner** : servlet을 관리하는 클래스. http 요청을 받아 servlet을 실행시킨다. 웹 서버와 통신 지원, 서블릿 생명주기 관리, 멀티쓰레드 지원 및 관리, 선언적인 보안 관리를 지원해준다.<br>
**was** : 서버 사이드 코드를 이용하여 동적인 컨텐츠를 생성하는 서버. WAS가 servlet container를 포함하는 개념이다.<br>
* FrontController 패턴

[기존의 servlet]
![image](https://user-images.githubusercontent.com/46257667/105040831-ab00eb80-5aa5-11eb-871e-fb9e79c218e4.png)

[FrontController 패턴]
![image](https://user-images.githubusercontent.com/46257667/105040992-d2f04f00-5aa5-11eb-8d1b-d7baa89db660.png)

사용자의 모든 요청에 대해 ```인코딩 처리```, ```에러 페이지 처리```, ```공지``` 등에 대한 처리를 한 곳에서 할 수 있다.

* spring MVC에서 servlet은 dispatcherSerlvet 하나만 사용하는 추세이다. 물론 여러 dispatcherServlet을 둘 수 있다.

