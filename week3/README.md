## :book: 강의 질문 내용 정리

### :smile: Spring 사용 이유?
- 자바는 복잡성을 해결할 수 있고 탄탄한 오픈소스가 많다.
- 대용량 트래픽이나 성능 부분에서 자바가 가진 이점
- 엔터프라이즈 환경에서 검증된 기술들이 많이 있다.

### :smile: 웹 애플리케이션 개발의 랜더링 방식
- 서버 사이드 렌더링
  - 서버에서 완전한 HTML을 만들어서 내려줍니다. 대표적으로 jsp, thymeleaf, velocity, freemarker가 있습니다.
  - 장점: 단순하고, 학습 곡선이 낮습니다. 백엔드 개발자도 쉽게 개발할 수 있습니다.
  - 단점: 동적이면서 복잡한 화면을 만들기 어렵습니다.

- 클라이언트 사이드 렌더링
  - 서버는 API만 제공하고, 자바스크립트 프레임워크가 템플릿과 서버 API 응답 결과를 조합해서 HTML 화면을 동적으로 만듭니다. 대표적으로 react, vue.js, angularJS 등이 있습니다.
  - 장점: 동적이고, 복잡한 화면을 만들기 좋습니다.
  - 단점: 공부할 분량이 매우 많습니다. 자바스크립트에 능숙해야 합니다. 웹 프론트엔드 개발자라는 전문 분야가 있습니다.

- 선택지
  - 자바스크립트에 자신있고, 완전 풀스택으로 갈것이다. = react + typescript
  - 백엔드 개발이 좋고, 스프링이 좋은데, 어쩔 수 없을 때 화면을 찍겠다. = thymeleaf
  - 레거시 하는 회사에 입사해야 한다. = jsp (ㅠㅠ)
  - velocity는 그냥 그걸 사용하는 회사에 입사하고 고민해도 됩니다. (쉽다)
  
### :smile: <p th:text="'안녕하세요'" + ${data}" > 안녕하세요. 손님</p>
-  손님 부분은 기본 값이라고 이해하시면 됩니다. th:text 부분이 처리될 수 있는 서버 환경에서는 th:text의 값으로 기본 값의 내용을 변경해버립니다.
- 서버가 없는 환경에서는 해당 html파일을 웹 브라우저에서 파일로 바로 열어보시면 안녕하세요. 손님이 출력되는 것을 확인할 수 있습니다. 서버가 없는 환경이기 때문에 ht:text 부분이 동작하지 않습니다. 이때는 순수한 html을 그대로 열어본 것 처럼 기본 값이 출력됩니다.
- 이렇게 해서 서버 없이 html 파일만 열었을 때도 기본값을 통해 결과를 볼 수 있는 장점이 있습니다.
- JSP 같은 경우에는 JSP 파일을 서버 없이 단순히 파일로만 열어보면 그 안에 프로그래밍 로직이 들어가기 때문에 html이 다 깨져보입니다. thymeleaf는 이런 메커니즘으로 html파일을 열어 볼 수 있는 장점이 있습니다.

### :smile: @RequestParam

- @RequestParam을 쓰지 않아도 쿼리스트링으로 받은 값을 사용할 수 있다.
  - 쿼리스트링 : ~?name=asdf,   public String helloMvc(String name)  이 경우 String name에 asdf이 전달됨.
  - 단, 쿼리스트링의 key값이 String name과 다를 경우(name이 아닐 경우) String name에는 null이 전달됨.
  - ~?nme=asdf,  public String helloMvc(String name) -> String name == null

- 영한님의 두번째 답변처럼 @RequestParam 뒤에 오는 String name은 컴파일 이후 변수명이 변경되어 혼동을 초래할 수 있음. 이를 방지하기 위해 @RequestParam("name")으로 쿼리스트링의 key값은 name이라는 것을 명확하게 표현.
  - @RequestParam("name")을 사용했을 경우 쿼리스트링에서는 반드시 ~?name=asdf으로 사용하여야 함.-> 만약 @RequestParam("name") String nme 이 상황에서 ~?nme=asdf으로 받으면 에러 발생. RequstParam을 썼으면 반드시 RequestParam과 key값을 맞춰야 함.
  - RequestParam없이 그냥 String name만 했을 때는 이름을 다르게 받으면 그냥 null값이 전달되었으나 RequestParam을 사용했을때 이름을 다르게 받으면 null전달이 아니라 에러가 뜨는 이유?
    - RequestParam은 key값을 명확하게 하기 위한 용도로 명시된 key값을 반드시 사용해야만 한다는 강제성을 부여. 반드시 사용해야하는 key값을 사용하지 않았기 때문에 null전달이 아니라 에러 발생한 것. (@RequestParam의 required 변수의 기본값이 true)

- @RequestParam 안에 다른 매개변수들이 들어올 수도 있음. 
  - @RequestParam(name, defaultValue, required, value)
  - name : 강의에서 사용한 파라미터
  - defaultValue : name이 전달되지 않을 경우 전달해줄 기본값
  - required : name 값의 강제성 여부 (디폴트는 true)
  - value : name과 같은 역할을 하는 name의 alias. 단, name과 value를 동시에 쓸 수 없음. 

- @RequestParam은 클라이언트가 보낸 HTTP 요청 쿼리 정보를 서버에서 받을 수 있는 방법입니다.
  - @RequestParam("name")이라고 되어 있으면 http://localhost:8080/~?name= 여기에서 name을 매칭해서 값을 읽어옵니다.
  - 추가로 @RequestParam(name ="name")이라고 해도 되고, 애노테이션에 파라미터가 하나만 있으면 다음과 같이 줄일 수 있습니다. @RequestParam("name")
  
### :smile:  domain은 entity + 2번째 의미의 vo들의 모음

- VO
  - 단순히 데이터 값을 전달하기 위한 용도로 사용되는 객체라는 뜻 (DTO와 같은 뜻으로 혼용해서 사용합니다. 저는 이 경우 DTO라고 합니다. DTO = 데이터를 전송하는 목적으로 사용하는 객체이지요)
  - 도메인 주도 설계에서 이야기하는 값 객체(Value Object)의 의미가 있습니다. 이 부분에 대한 자세한 내용은 JPA 기본편 강의에서 자세히 설명드립니다.(저는 vo라고 하면 이 의미로 사용합니다.)
- repository := dao (비슷함)
  - 이 둘은 거의 같다고 생각하셔도 무방합니다. 좀 더 깊이있게 차이를 설명하면, repository는 엔티티 객체를 보관하고 관리하는 저장소이고, dao는 데이터에 접근하도록 DB접근 관련 로직을 모아둔 객체입니다. 둘다 개념의 차이일뿐 실제로 개발할 때는 비슷하게 사용됩니다.
  
### :smile: 다형성

자바는 객체 지향 언어이고, 객체 지향 언어의 꽃은 바로 다형성(Polymorphism)에 있습니다.
여기서는 단순하게 Map store = new HashMap() 이렇게 선언했는데요.
이 부분만 보면 HashMap store = new HashMap()와 비교해서, 이점은 다음과 같습니다.

1. Map 인터페이스의 제약을 따르겠다는 의도를 명확하게 드러냅니다.

2. 사용하는 코드가 Map 인터페이스 제약을 따르기 때문에 향후 변경시에 사용코드를 변경하지 않아도 됩니다.

3. HashMap을 다른 클래스로 변경이 필요하면 선언하는 코드만 변경하면 됩니다. 사용하는 코드를 고민하지 않아도 됩니다.

4. 다른 개발자들이 이 코드를 나중에 더 성능이 좋거나 동시성 처리가 가능한 종류의 구체적인 Map으로 변경해야 할 때 HashMap store = new HashMap()이라고 되어 있다면, 변경 시점에 상당히 많은 고민을 해야 하지만 Map store = new HashMap()으로 선언이 되어 있다면 편안하게 선언부를 변경할 수 있습니다.

5. 개발은 무의미한 자유도를 제공하는 것 보다, 제약을 부여하는 것이 혼란을 줄이고, 유지보수하기 쉽습니다.

6. 만약 정말 HashMap의 구체적인 기능을 사용해야 한다면 HashMap store = new HashMap() 이라고 선언하는 것이 맞습니다.

여기까지는 클래스 내부에서 사용하는 것에 대한 부분입니다.

여기서 한발 더 나가면 다음과 같은 설계도 가능합니다.

```java
class MemberRepository {

  private Map store;

  public MemberRepository(Map store) {

    this.store =store

  }

}

외부에서 선언하는 코드

new MemberRepository(new HashMap());

new MemberRepository(new ConcurrentHashMap());
```

이렇게 해서 MemberRepository를 전혀 변경하지 않고, 외부에서 구현 객체를 생성해서 파라미터로 넘길 수 있습니다. 이런 것을 의존관계 주입( DI )이라고 하는데요. 이런 방법도 다형성 덕분에 사용할 수 있습니다.

### :smile: 필드 주입 vs 생성자 주입

- 필드 주입시 MemberService를 바꿀 수 없다는 의미는, 이미 주입된 객체를 실행 중간에 바꾼다는 의미라기 보다는 다음과 같은 의미가 있습니다.
테스트 실행시 스프링 컨테이너의 도움 없이 MemberService가 가지고 있는 여러 Repository를 자유롭게 변경하면서 테스트 할 수 있어야 합니다. 그런데 필드 주입을 사용하면, 스프링 컨테이너가 없을 때 의존하는 객체를 변경할 수 있는 방법이 없습니다.
- 반면에 생성자 주입에서는 스프링 컨테이너의 도움 없이 직접 new MemberService(new XxxRepository)와 같은 식으로 스프링 컨테이너의 도움 없이 원하는 객체를 변경해서 테스트 하거나 실행할 수 있습니다.


