### :smile: spring boot MVC 전체 동작 과정
```
'전체 동작 파악을 우선적으로, 세부 동작은 천천히' 
```
![image](https://user-images.githubusercontent.com/46257667/103460661-7c6fda80-4d5b-11eb-9e64-efb724d12bc9.png)

#### :book: Dispatcher Servlet 중심 (🔴원) 
1️⃣:브라우저로부터 요청이 들어오면 Servlet Container(톰캣 내장 서버)가 생성한 Dispatcher Servlet이 그 요청을 가로챈다. <br>
2️⃣,3️⃣:Dispatcher Servlet은 그 요청을 가지고 Handler Mapping에게 해당 요청을 어느 controller method 에게 위임할지 물어본다. <br>
4️⃣:Dispatcher Servlet은 실행할 controller method정보(HandlerMethod)를 Handelr Adapter에게 전달합니다.Handler Adapter는 전달받은 controller method를 실행하는데, 실행하기 전에 HandlerInterceptorAdapter를 구현한 interceptor들을 먼저 실행한다.<br>
5️⃣:결과적으로 ViewName과 Model을 반환한다.<br>
6️⃣,7️⃣:Dispatcher Servlet은 Handler Adapter로부터 응답받은 ViewName과 Model을 View Resolver에게 위임하여, response body가 될 view(html)를 응답받는다.<br>
8️⃣: client에게 view 반환하게 된다.<br>
#### :book: Controller 중심 (🟡원) 
1️⃣:Handler Interceptor는 DispatcherServlet이 컨트롤러를 호출하기 전과 후에 요청, 응답을 가공할 수 있는 일종의 필터이다. Handler Adapter가 아래와 같이 구현되어있는 preHandle과 postHandle을 controller 호출 전후에 호출한다.
```java
public interface HandlerInterceptor {
	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

	void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception;

	void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception;
}
```
2️⃣:HandlerMapping을 통해 찾은 컨트롤러를 직접 실행한다.<br>
3️⃣,4️⃣,5️⃣,6️⃣,7️⃣:controller -> service -> repository -> DB 순으로 **일반적인 웹 계층구조**를 따라 실행된다.<br>
8️⃣:객체를 Handler Adaptor에 반환한다.<br>
9️⃣:반환된 객체를 후처리해야하는 경우 Handler Interceptor에 보내 후처리 후 객체를 반환 받는다.<br>

### :smile: spring boot MVC 계층구조(domain, DTO, DAO 중심)
![image](https://user-images.githubusercontent.com/46257667/103479010-c9ac8480-4e0d-11eb-83c3-277b8c60e514.png)

- **Domain(Entity)**
entity class는 DB와 1:1 매칭을 할 수 있다. 이러한 Entity class를 Domain이라 하며 DB와 가장 가까운 class다.
```java
@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; // PK

    @NotEmpty
    @NotNull
    private String name;

    @Embedded // 내장 타입 임베딩
    private Address address;

    @JsonIgnore
    @OneToMany (mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
```
- **Repository(DAO)**
```java
@Repository 
@RequiredArgsConstructor
public class MemberRepository{

    private final EntityManager em; 

    public void save(Member member) {
        em.persist(member);
    }
}
```
persistence layer에 해당한다. <br>
Java Persistence API(JPA) 구현체를 이용하여 자바 객체로 접근할 수 있다. ex) Hibernate, MyBatis 등
- **DTO**
Entity를 통해 DB에서 데이터를 꺼내왔지만 한가지 문제가 있다.<br>
요청을 받고 데이터를 처리하고 반환해주기 위해선 데이터에 접근해야 하는데<br>
여기서 Controller, Presentation Layer의 경우 클라이언트와 직접 맞닿는 부분이고<br>
Entity는 Presentation Layer와 완전히 분리되어야 한다.<br>

이럴 때 DTO를 사용하는데,
```java
@Data
@AllArgsConstructor
static class MemberDto {
    private String name;
    private Address address;
}
```
Getter/Setter가 없고 wrapping된 순수한 데이터 객체이다. Entity에 직접 접근하지 않기때문에 Entity가 변경되어도 DTO만 변경하면 된다.

### :smile: 싱글톤 패턴(Singleton Pattern)

- 싱글톤 패턴은 클래스의 **인스턴스를 하나만 생성하고 사용**하는 형태이다.
- 대표적인 예로는 커넥션풀이 있다.
- *자바의 싱글톤 패턴과 스프링의 싱글톤 패턴은 다르다.*

#### :book: 자바 싱글톤 패턴
```
클라이언트에서 요청이 들어올 때 하나의 인스턴스가 생성 되는 프로그램이 있다.
10번의 요청이 들어왔을 때 10개의 인스턴스가 생성되었다.
이 프로그램은 이대로 사용을 해도 괜찮을까?
```

- 위와 같은 상황에서 만약 100번, 1000번 그 이상의 요청이 들어온다면 어떻게 될까? 
GC(Garbage Collector)가 사용중이지 않은 인스턴스를 정리해준다고 해도 *서버에 많은 부하가 가고 메모리가 많이 소모될* 것으로 예상된다. 
**싱글톤 패턴은 이러한 경우에 사용한다.** 인스턴스가 여러군데에서 사용되지만 해당 인스턴스가 *한번만 생성되면* 되는 경우에 유용한 디자인 패턴이다.
그렇기 때문에 **생성자가 여러 차례 호출되어도 최초 생성한 객체를 반환하여 서버 부하를 방지하고 메모리 낭비를 방지할 수 있다.**

- 아래는 (싱글톤 패턴이 적용되기 전)프린터 연결과 관련된 예제를 만들어보았다. 프린터를 호출하여 연결하는 것을 Printer 클래스를 인스턴스화 하는 것이라고 가정해보자.

- 사용자가 문서 출력을 위해 프린터에 연결을 시도했다. 얼마 후 다시 프린터에 연결을 시도했을 때, *새로운 프린터와* 연결이 되었다. (??)

```java
package spring.chap1.singleton;
 
public class Printer {
        
    public Printer() {
            System.out.println("## 프린터 생성 ##");
        }    
}
```
```java
package spring.chap1.singleton;

public class User {

        /**
         * @param args
         */
        public static void main(String[] args) {
                Printer pt = new Printer();
                System.out.println("프린터 연결 시도 >>> " + pt.hashCode());
                System.out.println("1분 후........... ");
                pt = new Printer();
                System.out.println("프린터 연결 시도 >>> " + pt.hashCode());
        }
}
```
![이미지 1](https://user-images.githubusercontent.com/47052106/103310852-010ce100-4a5c-11eb-9c6e-f5dd1bcc2208.png)

- 두 해쉬코드가 서로 상이한 것을 보아 기존의 프린터가 아닌 *다른 프린터와 연결이* 달리 되었음을 알 수 있다.
**이런 경우 프린터 인스턴스는 하나 이상을 필요로 하지 않는다.**
이미 프린터와 연결이 되어있는데 굳이 다른 프린터에 연결을 하려는 것은 **비효율적인 일**이기 때문이다.
*그럼 어떻게 하면 프린터를 호출할 때 하나만의 인스턴스를 유지할 수 있을까?*
여기에 일반적으로 잘 알려진 **Lazy Instantiation라고 불리는 싱글톤 패턴**을 적용해보았다.

```java
package spring.chap1.singleton;
 
public class PrinterLazy {
        private static PrinterLazy pl = null;
        
        private PrinterLazy(){
            System.out.println("## 프린터 생성 ##");
        }
        
        public static PrinterLazy getInstance(){
            if(pl == null){
                pl = new PrinterLazy();
            }       
            return pl;
        }
}
```

*생성자를 private 으로 선언하기 때문에 접근이 불가능하게 된다.* 따라서 외부에서 new를 통해 인스턴스 생성이 불가능하게 되었다. 대신 public static으로 선언된 getInstance() 메소드를 호출해서 인스턴스가 null일 경우 새로운 객체를 생성하여 인스턴스를 받아온다.

```java
package spring.chap1.singleton;
 
public class User {
 
        /**
         * @param args
         */
        public static void main(String[] args) {
                PrinterLazy pl = PrinterLazy.getInstance();
                System.out.println("프린터 연결 시도 >>> " + pl.hashCode());
                System.out.println("1분 후........... ");
                pl = PrinterLazy.getInstance();
                System.out.println("프린터 연결 시도 >>> " + pl.hashCode());
        }
}
```

![이미지 2](https://user-images.githubusercontent.com/47052106/103310908-2bf73500-4a5c-11eb-8094-f12a896e4f65.png)

하지만 이 코드에는 문제점이 있다. 

- **멀티 스레드2 환경에서 Printer 인스턴스의 사용이 병렬적으로 발생한다면 인스턴스가 여러번 생성되는 경우가 발생할 수 있다.**

![그림1](https://user-images.githubusercontent.com/47052106/103310940-46311300-4a5c-11eb-817d-eb204710ae1f.png)

Printer 인스턴스가 생성되지 않았을 때 스레드 1이 getInstance() 메소드를 호출해 인스턴스를 생성하려고 한다.
이 때, *스레드1의 인스턴스가 생성되기 직전* 스레드 2가 마찬가지로 getInstance() 메소드를 호출해 인스턴스의 미생성으로 인해 인스턴스를 생성하는 일이 벌어졌다. 
이렇게 되면 싱글톤 패턴의 핵심이 지켜지지 못한다. **인스턴스가 2개 이상이 될 수 있는 것이다.**
이 문제를 멀티 스레드 환경에 안전하지 못하다고 하는데 이를 해결하기 위한 방법들이 있다.

- **동기화**

```java
package spring.chap1.singleton;
 
public class Printer {
    private static Printer pt = null;    
 
    private Printer(){
        System.out.println("## 프린터 생성 ##");
    }        
 
    public static synchronized Printer getInstance(){
        if(pt == null) {            
            pt = new Printer();
        }
        return pt;
    }
}
```

getInstance() 메소드를 **동기화**시킴으로써 한 스레드의 사용이 끝나기 전까지 *다른 스레드는 기다리게 되었다.* 따라서 getInstance() 메소드가 **동시에 호출될 일이 없어진다.**
하지만 해당 메소드가 실행되는 경우 동기화를 위해 스레드의 Lock을 거는 행위로 인해 *프로그램의 성능이 저하된다.*

- **Eager initialization**

```java
package spring.chap1.singleton;
 
public class Printer {
    private static Printer pt = new Printer();
    
    private Printer(){
        System.out.println("## 프린터 생성 ##");
    }        
    
    public static synchronized Printer getInstance(){
        return pt;
    }
}
```

이 방법은 인스턴스의 생성 유무와 관계없이 **초기에 인스턴스를 생성**하는 방식이다. 
이 방법 역시 멀티 스레드 환경의 문제점을 해결할 수 있다. 하지만 초기 인스턴스 생성으로 인해 *프로그램의 시작과 종료까지 객체가 메모리에 상주하게 된다.* 때문에 **사용유무에 따라 비효율적인 문제가 있다.**

- **DCL(Double-Checking Locking) + volatile**

```java
package spring.chap1.singleton;
 
public class Printer {
    private volatile static Printer pt = null;
 
    private Printer(){
        System.out.println("## 프린터 생성 ##");
    }        
    
    public static Printer getInstance(){
        if(pt == null) {
            synchronized (Printer.class) {
                if(pt == null){
                    pt = new Printer();
                }
            }
        }
        return pt;
    }
}
```

DCL은 인스턴스를 체크하여 인스턴스가 null일 경우에만 동기화를 한다. 즉, *최초 인스턴스 생성시에만 동기화 블럭에 진입하게 되면서 이후로는 동기화되지 않는다.* 하지만 **volatile3을 사용하지 않게 되면** 멀티 스레드 환경에서 각 스레드마다 기억하는 변수의 값이 다를 수 있어(왜냐하면 각 스레드마다 동일한 메모리를 공유하지 않기 때문) **인스턴스의 동기화가 보장되지 않는다.**

#### :book: 스프링 싱글톤 패턴

- 스프링 싱글톤은 클래스 자체에 의해서가 아니라, **스프링 컨테이너**(Bean Factory/Application Context)에 의해 구현된다.
- 스프링에서는, 컨테이너 내에서 특정 클래스에 대해 @Bean이 정의되면, *스프링 컨테이너는 그 클래스에 대해 딱 한 개의 인스턴스를 만든다.* 이 공유 인스턴스는 설정 정보에 의해 관리되고, **bean이 호출될 때마다 스프링은 생성된 공유 인스턴스를 리턴 시킨다.(중요)** 
- 공유인스턴스의 생성시점은 스프링 컨테이너에 따라 달라지는데, 빈팩토리는 최초 호출시점에서 인스턴스를 생성하고(Lazy loading), 애플리케이션 컨텍스트는 미리 모든 공유인스턴스를 다 초기화해 두었다가 호출될때 바로 리턴시켜준다. 
- bean 관리의 주체인 스프링 컨테이너는 그 어떤 호출에도 **단일 공유 인스턴스를 리턴**시키므로 *thread safety도 자동으로 보장된다.*

![img](https://user-images.githubusercontent.com/47052106/103311192-09b1e700-4a5d-11eb-8245-576441491e72.png)

### :smile: DI (Dependency Injection)

#### :book: DI란?
* DI는 **의존성 주입**을 의미하며, 한 클래스가 다른 클래스의 메서드를 실행하기 위해 의존성을 설정하는 것이다.
* 객체의 구성 요소에 필요한 설정 등의 의존성을 코드에서 분리하고 외부에서 주입하도록 하자는 것이 의존성 주입의 기본적인 원리이다.
* 객체 자체에서 의존성을 설정하는 것이 아닌, Spring Framework에 의해 의존성이 주입되는 설계 패턴이다.
  * Dependency Injection은 Spring Framework에서 지원하는 IoC의 형태
  * IoC(Inversion of Control) : 객체의 제어권이 개발자에서 컨테이너로 역전되었다는 의미 (제어의 역전)
  * Spring Framework에서는 IoC 컨테이너 기능을 제공하는데, IoC 컨테이너에서는 객체 생성 및 생명주기를 관리한다.
  <img src="https://user-images.githubusercontent.com/61045469/103369482-961be280-4b0d-11eb-9dd3-c526e5f0bad5.PNG" width="50%" height="30%"></img><br/>

#### :book: DI의 장점
* 스프링 컨테이너가 객체를 관리해주기 때문에 개발자의 부담이 줄어든다.
* 객체 간의 결합도가 낮아져 상호 간 의존성 관계를 줄여준다.(약한 결합)
* 코드의 재사용이 용이하고 코드가 분리되어 가독성이 좋아진다.
* 코드의 유지보수가 용이하다.

#### :book: DI 종류 3가지
* **필드 주입(Field Injection)**
  ```java
  public class Sample {
      @Autowired
      private Example example;
  }
  ```
  * 가장 흔히 볼 수 있는 Injection 방법이나, 권장되지 않는다. 
  * 쉬운 의존성 주입 방법은 하나의 클래스에서 지나치게 많은 기능을 하게 만든다. 이는 '객체는 그에 맞는 동작만을 한다.'는 법칙(Single Responsibility Principle)에 위배된다.
  * 추상화된 의존관계는 의존성을 검증하기 힘들게 만든다. 
  * 필드 주입을 사용하면 해당 클래스를 바로 Instant화 시킬 수 없다.
  * 필드 주입된 객체는 final 선언을 할 수 없으므로 가변적이다.
  
* **Setter 주입(Setter Injection)**
  ```java
  public class Sample {
      private Example example;

      @Autowired
      public void setExample(Example example) {
          this.example = example;
      }
  }
  ```
  * 선택적인 의존성을 주입할 경우에 유용하며, Spring 3.X시대까지 제일 권장되던 방법이었다.
  * Optional Injection의 경우 권장되는 방식이다.
  
* **생성자 주입(Constructor Injection)**
  ```java
  public class Sample {
      private final Example example;

      @Autowired
      public Sample(Example example) {
          this.example = example;
      }
  }
  ```
  * 생성자에서 의존성을 주입하는 방법으로, Spring 4.X 이상부터 권장되는 방법이다.
  * 권장되는 이유는 필수적으로 사용해야 하는 레퍼런스(객체, 의존성) 없이는 인스턴스를 만들지 못하도록 강제하는 것이라고 할 수 있다.
  * 객체에 final 선언이 가능하여 Immutability(불가변성)을 보장한다.
  * 의존성의 순환 참조(Circular Dependency)에 대한 예방이 가능하다.
  * Spring 4.3 이상부터는 생성자가 하나인 경우 @Autowired를 사용하지 않아도 무방하다.
  
  
#### :book: Spring DI Container
* Spring DI Container가 관리하는 객체를 Bean이라 하고, Bean들을 관리하는 의미로 Container를 BeanFactory라고 부르기도 한다.
  <img src="https://user-images.githubusercontent.com/61045469/103369341-39b8c300-4b0d-11eb-8968-ac72cb8dec16.PNG" width="50%" height="30%"></img><br/>

## :smile: 라이브러리
- thymeleaf : Thymeleaf는 템플릿 엔진으로 스프링 프레임워크의 MVC 구조에서 V 즉, 뷰(View)를 담당하는 라이브러리이고, 텍스트 템플릿 엔진입니다. 템플릿 양식과 특정 데이터 모델에 따른 입력 자료를 결합하여 원하는 결과 문서를 출력하는 소프트웨어(또는 컴포넌트)를 말합니다. 그중 웹 템플릿엔진이란 웹문서가 출력되는 엔진을 말합니다. 종류로는 Freemarker, Thymeleaf, JSP(Java Server pages) 등이 있습니다. 그 중 Thymeleaf는 비교적 최근에 만들어진 템플릿 엔진이며 서버사이드 자바 템플릿 엔진의 한 종류입다.JSP와 Thymeleaf의 가장 큰 차이점은 JSP와 달리 Servlet Code로 변환되지 않다는 점이다. 따라서 비즈니스 로직과 분리되어 오로지 View에 집중할 수 있고 의존성이 높다는 장점이 있습니다.

- Gradle
    - Ant처럼 유연한 범용 빌드 도구
    - Maven을 사용할 수 있는 변환 가능 컨벤션 프레임 워크
    - 멀티 프로젝트에 사용하기 좋음
    - Apache Ivy에 기반한 강력한 의존성 관리
    - Maven과 Ivy 레파지토리 완전 지원
    - 원격 저장소나, pom, ivy 파일 없이 연결되는 의존성 관리 지원

    - 그루비 문법 사용 
    - 빌드를 설명하는 풍부한 도메인 모델 
    
# About Log
[LOG페이지](%EB%B0%95%EC%9C%A4%ED%99%98/Log.md)

