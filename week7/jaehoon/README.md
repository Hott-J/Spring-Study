# GIVEN-WHEN-THEN [준비-실행-검증]
SpecificationByExample을 사용한 시스템 행동방식을 구체화한 테스트를 대표하는 스타일이다. 이 방법은 Behavior-Driven(BDD) 중 하나로 Daniel Terhorst-North와 Chris Matts에 의해 개발되었다.

given - 테스트에서 구체화하고자 하는 행동을 시작하기 전에 테스트 상태를 설명하는 부분이다.
when - 구체화하고자 하는 그 행동이다.
then - 어떤 특정한 행동 때문에 발생할거라고 예상되는 변화에 대해 설명하는 부분.

```java
@Before
public void init(){
    // given - 테스트를 위한 데이터 생성
    User user = userRepository.save(User.builder().name("havi")
    .password("test")
    .email(email)
    .createdDate(LocalDateTime.now())
    .build());

    boardRepository.save(Board.builder()
    .title(boardTestTitle)
    .subTitle("서브 타이틀")
    .content("콘텐츠")
    .boardType(BoardType.free)
    .createdDate(LocalDateTime.now())
    .updateDate(LocalDateTime.now())
    .user(user).build());

    @Test // BDD 스타일 중 하나로 "Test method names should be sentences" 를 지키기 위해 method 이름을 문장으로 작성.
    public void 제대로_생성됐는지_테스트(){
        // when - 구체화하고자 하는 행동
        User user = userRepository.findByEmail(email);

        // then
        assertThat(user.getName(), is("havi"));
        assertThat(user.getPassword(), is("test"));
        assertThat(user.getEmail(), is(email));

        // when
        Board board = boardRepository.findByUser(user);

        // then
        assertThat(getTitle(), is(boardTestTitle));
        assertThat(getSubTitle(), is("서브 타이틀"));
        assertThat(getContent(), is("콘텐츠"));
        assertThat(getBoardType(), is(BoardType.free));
    }
}
```

# JpaRepository 메소드 추가

>선언만으로 자동으로 메소드를 생성해주는 JpaRepository

```java
public interface UserRepository  extends JpaRepository <User, Long> {
    User findByEmail(String email);
    User findByPassword(String password);
}
```

findByPassword 메소드를 만들었다. 

```java
private final String password = "test";


@Test
public void is_generated_test() {

    //User user = userRepository.findByEmail(email);
    User user = userRepository.findByPassword(password);

    assertThat(user.getName(), is("havi"));
    assertThat(user.getPassword(), is("man"));
    assertThat(user.getEmail(), is(email));
}
```

password가 test인 user 하나를 만들어서 해당 user를 받아오려 했다. 그런데 웬걸 error가 발생했다.

![image](https://user-images.githubusercontent.com/46257667/107156668-106c3c00-69c3-11eb-9ecb-05eb5d3b6055.png)

`query did not return a unique result: 2;` 라는 에러인데 password로 'test'를 갖고 있는 user가 2개라는 것이다. 생각해보니 어플을 실행할 때 CommandLineRunner로 user 하나를 생성했었는데 그 user와 같은 password를 쓰고있기 때문에 에러가 났다. 해결법은
* List로 받기
* 기존의 user 지우기

# JpaRepository 메소드 명명 규칙

* `findByXX`
기본형이다. "findBy" 이후에 엔티티의 속성 이름을 쓰면 된다. 속성 이름의 첫글자는 대문자이어야 한다.
* `Like / NotLike`
"퍼지 검색"에 관한 것이다. Like를 붙이면, 인수에 저장된 텍스트를 포함하는 엔티티를 검색한다. "findByNameLike"이라면, name에서 인수의 텍스트를 퍼지 검색한다. 예를 들어 findByNameLike("%ko")는 name 값이 "ko"로 끝나는 엔티티가 검색된다.
* `StartingWith / EndingWith`
텍스트 값에서 인수에 지정된 텍스트로 시작하거나 끝나는 것을 검색하기 위한 것이다. findByNameStartingWith("A")이라면, name의 값이 "A"로 시작하는 항목을 검색한다.
* `IsNull / IsNotNull`
값이 null이 거나, 혹은 null이 아닌 것을 검색한다. 인수는 필요없다. "findByNameIsNull()"이라면, name의 값이 null의 것만 검색한다.
* `True / False`
부울 값으로 true 인 것, 혹은 false 인 것을 검색한다. 인수는 필요없다. "findByCheckTrue()"이라면, check라는 항목이 true 인 것만을 검색한다.
* `Before / After`
시간 값으로 사용한다. 인수에 지정한 값보다 이전의 것, 혹은 이후 것을 검색한다. "findByCreateBefore(new Date())"라고 하면, create라는 항목의 값이 현재보다 이전의 것만을 찾는다 (create가 Date 인 경우).
* `LessThan / GreaterThan`
숫자 값으로 사용한다. 그 항목의 값이 인수보다 작거나 큰 것을 검색한다. "findByAgeLessThan(20)"이라면, age의 값이 20보다 작은 것을 찾는다.
* `Between`
두 값을 인수로 가지고 그 두 값 사이의 것을 검색한다. 예를 들어, "findByAgeBetween(10, 20)"라고 한다면 age 값이 10 이상 20 이하인 것을 검색한다. 수치뿐만 아니라 시간의 항목 등에도 사용할 수 있다.

# CommandLineRunner
>초기화 코드 넣는 방법

```java
@SpringBootApplication public class SpringinitApplication { 
    public static void main(String[] args) { 
        SpringApplication.run(SpringinitApplication.class, args); 
        System.out.println("Hello Spring Boot Application"); 
    } 
}
```

위와 같이 코딩해도 틀렸다고 할 순 없지만 이미 생성한 빈들을 사용하고자 할 때 의존성 주입을 하기 상당히 애매하다는 점이 있다. 이럴 때 필요한 것이 CommandLineRunner이다.

CommandLineRunner 인터페이스를 구현한 클래스를 스프링 빈으로 등록하게되면, 스프링이 초기화 작업을 마치고 나서 해당 클래스의 run(String... args) 메서드를 실행시켜주는 방법이다.

```java
@Bean public CommandLineRunner myCLRunner() { 
    return args -> System.out.println("Hello Lambda CommandLineRunner!!");
}
```

CommandLineRunner가 @FunctionalInterface(추상 메서드가 하나인 인터페이스)이기 때문에 람다(lambda)를 사용할 수 있다.

```java
@Bean public CommandLineRunner myCLRunner(MyController myController) { 
    return args -> myController.hello(); 
}
```
위와 같이 파라미터로 빈을 넣어주면 메소드 안에서 사용할 수 있다.