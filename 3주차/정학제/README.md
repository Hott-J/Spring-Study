# :cherry_blossom: Spring 입문

---

## :one: 강의소개

### :smile: 강의소개

- 스프링 프레임워크에 대한 핵심 개념!!!
  - 실제 예제 코드를 직접 해본다.

### :smile: 프로젝트 세팅

- 자바 1.8 다운 후, Project Structure에서 Project SDK 와 Modules의 dependencies를 1.8로 세팅한다.
- css를 변경했을 시에는 maven으로 실행해야 변경된 css도 적용됨
  - css 말고 java 코드 변경 시에는 그냥 main 메소드 실행해도 변경된 사항 적용된다.

### :smile: 프로젝트 살펴보기

- Owner -> Pet -> Visit
- Vet -> Specialty

## :two: Inversion of Control

### :smile: IOC 소개

- 내가 쓸 것은 내가 쓴다. 일반적인 의존성에 대한 제어권

```java
class OwnerController {
  private OwnerRepository repository = new OwnerRepository();
}
```

- 내가 쓸 것이 이것인데, 누군가 알아서 주겠지. (IOC)
  - 내가 쓸 것의 타입만 맞으면 어떤 것이든 상관없다.
  - 그래야 코드 테스트 하기도 편하다.
```java
class OwnerController {
  private OwnerRepository repo;
  public OwnerController(OwnerRepository repo) {
    this.repo = repo;
  }
// repo를 사용합니다.
}
class OwnerControllerTest {
@Test
  public void create() {
    OwnerRepository repo = new OwnerRepository();
    OwnerController controller = new OwnerController(repo);
  }
}
```
- **IOC 컨테이너가 이를 관리해준다.**

### :smile: IOC (Inversion Of Control) 컨테이너

- *ApplicationContext (BeanFactory)* 라고도 한다.
- **빈**을 만들고 엮어주며 제공해준다.
  - 빈은 @Component , @Service, @Repository, @Controller 와 같은 애노테이션을 보고 알 수 있다.
    - 컴포넌트 스캔
 
### :smile: 빈(Bean)

- 스프링 IoC 컨테이너가 관리하는 객체
- *등록하는 방법*
  - **Component Scanning**
    - @Component
    - @Repository
    - @Service
    - @Controller
  - **직접 일일히 XML이나 자바 설정 파일에 등록**
    - @Bean 으로 함수 작성
      - @Bean은 @Configuration 안에서 사용해야한다.
      - @SpringBootApplication안에는 @Configuration이 있다. 

- *사용법*
  - @Autowired 또는 @Inject
    - @Autowired는 이름을 보면 알 수 있듯이 이 애노테이션을 부여하면 각 상황의 타입에 맞는 IOC컨테이너 안에 존재하는 Bean을 자동으로 주입해주게 된다.
  - ApplicationContext에서 getBean()으로 직접 꺼내 사용

- **오로지 “빈"들만 의존성 주입을 해줍니다.**

### :smile: 의존성 주입 (Dependency Injection)

- 생성자가 오로지 한 개만 있고, 매개변수가 빈으로 등록이 되어있다면 @Autowired는 생략가능하다. 
  - 빈을 사용하기 위해서는 먼저 빈으로 등록하고 등록된 빈으로 빈을 사용해야한다.
- 스프링데이터JPA의 Repository 인터페이스로 인터페이스 객체를 구현하면, 이 객체 역시 빈이 된다.
- 생성자
  - 가장 추천
- 필드
  - setter가 없다면
- setter
  - setter가 있다면, 없으면 굳이 만들어서 할 필요없다. setter를 통해 변경이 이뤄지는 것은 안좋으므로.
  
## :three: Aspect Oriented Programming
  
### :smile: AOP 소개

- *흩어진 코드를 한 곳으로 모아준다.*

- 흩어진 AAAA 와 BBBB
```java
class A {
  method a () {
    AAAA
    오늘은 7월 4일 미국 독립 기념일이래요.
    BBBB
  }
  method b () {
    AAAA
    저는 아침에 운동을 다녀와서 밥먹고 빨래를 했습니다.
    BBBB
  }
}

class B {
  method c() {
    AAAA
    점심은 이거 찍느라 못먹었는데 저녁엔 제육볶음을 먹고 싶네요.
    BBBB
  }
}
```

- 모아 놓은 AAAA 와 BBBB
```java
class A {
  method a () {
    오늘은 7월 4일 미국 독립 기념일이래요.
  }
  method b () {
    저는 아침에 운동을 다녀와서 밥먹고 빨래를 했습니다.
  }
}

class B {
  method c() {
    점심은 이거 찍느라 못먹었는데 저녁엔 제육볶음을 먹고 싶네요.
  }
}

class AAAABBBB {
  method aaaabbb(JoinPoint point) {
    AAAA
    point.execute()
    BBBB
  }
}
```
- 스프링은 Proxy 패턴을 통해 AOP를 구현한다.
- 객체 지향의 single 

### :smile: AOP 적용 예제

- @LogExecutionTime 애노테이션 (어디에 적용할지 표시 해두는 용도)
```java
@Target(ElementType.METHOD) // 메소드에 붙일 것이다.
@Retention(RetentionPolicy.RUNTIME) // 에노테이션 사용한 코드를 언제까지 유지할 것이냐.
public @interface LogExecutionTime {
}
```
- 실제 Aspect (@LogExecutionTime 애노테이션 달린곳에 적용)
```java
@Component
@Aspect
public class LogAspect {
  Logger logger = LoggerFactory.getLogger(LogAspect.class);
  @Around("@annotation(LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Object proceed = joinPoint.proceed();
    stopWatch.stop();
    logger.info(stopWatch.prettyPrint());
    return proceed;
  }
}
```

- *Aspect*
  - 애스펙트는 부가기능을 정의한 코드인 어드바이스(Advice)와 어드바이스를 어디에 적용하지를 결정하는 포인트컷(PointCut)을 합친 개념이다.
  - **Advice + PointCut = Aspect**
  - AOP 개념을 적용하면 핵심기능 코드 사이에 침투된 부가기능을 독립적인 애스펙트로 구분해 낼수 있다.
  - 구분된 부가기능 애스펙트를 런타임 시에 필요한 위치에 동적으로 참여하게 할 수 있다.

- 타겟(Target)
  - 핵심 기능을 담고 있는 모듈로 타겟은 부가기능을 부여할 대상이 된다.

- 어드바이스(Advice)
  - 어드바이스는 타겟에 제공할 부가기능을 담고 있는 모듈이다.

- 조인포인트(Join Point)
  - 어드바이스가 적용될 수 있는 위치를 말한다.
  - 타겟 객체가 구현한 인터페이스의 모든 메서드는 조인 포인트가 된다.

- 포인트 컷(Pointcut)
  - 어드바이스를 적용할 타겟의 메서드를 선별하는 정규표현식이다.
  - 포인트컷 표현식은 execution으로 시작하고 메서드의 Signature를 비교하는 방법을 주로 이용한다.

- 애스펙트(Aspect)
  - 애스펙트는 AOP의 기본 모듈이다.
  - 애스펙트 = 어드바이스 + 포인트컷
  - 애스펙트는 싱글톤 형태의 객체로 존재한다.

- 어드바이저(Advisor)
  - 어드바이저 = 어드바이스 + 포인트컷
  - 어드바이저는 Spring AOP에서만 사용되는 특별한 용어이다.

- 위빙(Weaving)
  - 위빙은 포인트컷에 의해서 결정된 타겟의 조인 포인트에 부가기능(어드바이스)를 삽입하는 과정을 뜻한다.
  - 위빙은 AOP가 핵심기능(타겟)의 코드에 영향을 주지 않으면서 필요한 부가기능(어드바이스)를 추가할 수 있도록 해주는 핵심적인 처리과정이다.

- Spring AOP 특징
  - Spring은 프록시 기반 AOP를 지원한다.
    - Spring은 타겟(target) 객체에 대한 프록시를 만들어 제공한다.
    - 타겟을 감싸는 프록시는 실행시간(Runtime)에 생성된다.
    - 프록시는 어드바이스를 타겟 객체에 적용하면서 생성되는 객체이다.
  - 프록시(Proxy)가 호출을 가로챈다(Intercept)
    - 프록시는 타겟 객체에 대한 호출을 가로챈 다음 어드바이스의 부가기능 로직을 수행하고 난 후에 타겟의 핵심기능 로직을 호출한다.(전처리 어드바이스)
    - 또는 타겟의 핵심기능 로직 메서드를 호출한 후에 부가기능(어드바이스)을 수행하는 경우도 있다.(후처리 어드바이스)

  - Spring AOP는 메서드 조인 포인트만 지원한다.
    - Spring은 동적 프록시를 기반으로 AOP를 구현하므로 메서드 조인 포인트만 지원한다.
    - 핵심기능(타겟)의 메서드가 호출되는 런타임 시점에만 부가기능(어드바이스)을 적용할 수 있다.
    - 반면에 AspectJ 같은 고급 AOP 프레임워크를 사용하면 객체의 생성, 필드값의 조회와 조작, static 메서드 호출 및 초기화 등의 다양한 작업에 부가기능을 적용 할 수 있다.

- Spring AOP의 구현 방식
  - XML 기반의 POJO 클래스를 이용한 AOP 구현
    - 부가기능을 제공하는 Advice 클래스를 작성한다.
    - XML 설정 파일에 <aop:config>를 이용해서 애스펙트를 설정한다. (즉, 어드바이스와 포인트컷을 설정함)
  - @Aspect 어노테이션을 이용한 AOP 구현
    - @Aspect 어노테이션을 이용해서 부가기능을 제공하는 Aspect 클래스를 작성한다.
    - 이 때 Aspect 클래스는 어드바이스를 구현하는 메서드와 포인트컷을 포함한다.
    - XML 설정 파일에 <aop:aspectj-autoproxy />를 설정한다.
    
- Advice의 종류
  - Around 어드바이스
    - 타겟의 메서드가 호출되기 이전(before) 시점과 이후 (after) 시점에 모두 처리해야 할 필요가 잇는부가기능을 정의한다. 
    - Joinpoint 앞과 뒤에서 실행되는 Advice
  - Before 어드바이스
    - 타겟의 메서드가 실행되기 이전(before) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다
    - Jointpoint 앞에서 실행되는 Advice
  - After Returning 어드바이스
    - 타겟의 메서드가 정상적으로 실행된 이후(after) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다.
    - Jointpoint 메서드 호출이 정상적으로 종료된 뒤에 실행되는 Advice
  - After Throwing 어드바이스
    - 타겟의 메서드가 예외를 발생된 이후(after) 시점에 처리해야 할 필요가 있는 부가기능을 정의한다.
    - 예외가 던져 질때 실행되는 Advice
