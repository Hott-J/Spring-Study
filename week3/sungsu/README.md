# :cherries: 스프링 강의 입문-petClinic

---
## :tulip: 개요
### :one: 프로젝트 셋팅
- jdk 버전: 1.8 (9 이상 지원되지 않는 문법이 포함된다.)
- IDE : intelij
- 빌드 도구 : Maven

### :two: 프로젝트 살펴보기
- CRUD 형식의 프로젝트
- Owner -> Pet -> Visit, Vet -> Specialty
  
## :tulip: IOC(inversion of control)
### :one: 정의
- 제어권의 역전
- 의존성에 대한 컨트롤
- 나 의외의 누군가가 컨트롤을 해주는 것
- 개발자가 독점적으로 가지고 있던 제어권이 서블릿과 EJB를 관리하는 외부의 컨테이너로 넘어갔고 객체의 생성부터 생명주기의 관리까지 모든 객체에 대한 제어권이 바뀐 것을 IoC, 제어의 역전이라 하는것이다.
- 직접 제어
```java
public class A {

    private B b;

    public A()
        b = new B();
    }
}
- IOC
```public class A {

    @Autowired
    private B b;

}
```
- 변경에 유연한 코드 구조를 가져가기 위해서 사용
  
### :two: IOC 컨테이너
- 빈을 만들고 엮어주고 제공한다
- application context로 직접 만들어줄 수 있다.
- 여러가지를 상속한다.
    - ex)ApplicationContext context = new ClassPathXmlApplicationContext("config/bean.xml");
- getBean으로 꺼낼수있다.-> 요즘은 직접 쓰지 않는다.

### :three: 빈
- 빈을 구분하는 방법: IntelliJ에서 옆에 초록 원이 뜨면 빈이다.
- java로 직접 등록 -@Bean으로 @Configuration안에서만 가능
```java

@Bean
	public String keesun(){
		return "keesun";
}
```

- 꺼내쓰는법 :ApplicationContext안에서 getBean사용, autowired

### :four: 의존성 주입
- 생성자가 하나만있으면 autowired가 없더라도 있음
- 왠만하면 생성자로 사용하기

#### :smile: PetRepository가 스프링 데이터 JPA에서 빈으로 자동으로 등록된다고?
#### :smile: VisitController에서 두가지를 연결?
```java
public VisitController(VisitRepository visits, PetRepository pets) {
		this.visits = visits;
		this.pets = pets;
	}
```


### :five: AOP
- byte code를 사용하는 방법과 프록시 패턴을 사용하는 방법
    1. Dynamic Proxy (Reflection): 인터페이스를 클래스로 구현하여 사용하는 경우 -> 훨씬 유연하므로 많이 사용
    2. CGLIB (Byte Code Instrument)
- 대표적인 AOP는 transactional

### :six: PSA
- Portable service abstraction
- spring이 제공하는 대부분이 PSA
- 추상화 계층을 사용해서 어떤 기술을 내부에 숨기고 개발자에게 편의성을 제공해주는 것을 Service Abstraction이라 한다.

더하여 Service Abstraction으로 제공되는 기술을 다른 기술 스택으로 간편하게 바꿀 수 있는 확장성을 갖고 있는 것이 Portable Service Abstraction이다.
- PSA를 사용하여 코딩을 해야 TestCase를 작성하기 편하고 확장하거나 수정하기 편하다.

#### :smile: 예제1) transactional
- PlatformTransactionManager을 사용하여 transaction을 기술하고 있음
- PlatformTransactionManger의 여러가지 구현체가 바뀌더라도 transaction의 코드는 변하지 않음.

#### :smile: 예제2)caching
- CacheManger의 구현체를 바꾸더라도 EnableChacing을 Asepct하는 코드에 변화를 줄 필요가 없다.

#### :smile: 예제3)스프링 웹 MVC
- GetMapping에서 return되는 값이 reactive인지 servlet인지 알 수 없음. 이에 따라 개발자는 기술을 독립적으로 만들어 줄 수 있다. 