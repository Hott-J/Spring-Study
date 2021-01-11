# 1️⃣ Inversion of control (제어의 역전)

### 🔶 IoC 소개
📖 어떤 객체가 사용할 객체(의존관계인 객체)를 직접 선언하여 사용하는 것이 아니라, 어떤 방법을 사용하여 (ex. 생성자) 주입 받아 사용하는 것.
```java
/*
일반적인 제어권 : 자기가 사용할 의존성은 자기가 만들어서 사용
*/
@Service
public class CarService{
    private CarRepository carRepository = new CarRepository();
}

/*
Inversion of control :
다른 누군가가 의존성을 밖에서 주입. dependency injection
*/
@Service
public class CarService{
    // CarRepository를 사용하지만 직접 만들어서 쓰진 않는다.
    private CarRepository carRepository;

    /*
    생성자를 통해서 받아온다. 따라서 의존성을 관리하는 일은 CarService가 하는 일이 아니다. 누군가 밖에서 넣어준다.
    */
    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }
}
```

### 🔶 IoC 컨테이너

IoC 컨테이너가 하는 일 : 이러한 빈들 즉, 자기가 컨테이너 내부에 만든 객체들의 의존성을 관리해준다. OwnerController와 OwnerRepository를 예로 들면 OwnerController와 OwnerRepository는 Application Context 내부에서 빈이다. 그렇기 때문에 이 둘의 의존성은 IoC 컨테이너가 관리해준다. 오로지 빈만 관리해준다. Intellij에서 왼쪽 라인 번수 표시란에 콩이 붙어있으면 빈이다.

### 🔶 빈(Bean)

스프링 IoC 컨테이너가 관리하는 객체

어떻게 등록하는가?
* Component Scanning<br>
@Component 가 붙어있으면 스캐너가 빈으로 등록한다. 예를 들어 @Controller는 @Component 가 붙어있기 때문에 빈으로 등록된다. 
* 직접 일일히 XML이나 자바 설정 파일에 등록

❗ @(어노테이션)은 기능이 있는 것이 아니다. 주석과도 같은 것. 어노테이션을 마커로 사용해서 그것을 처리하는 processor가 있는 것이다.

### 🔶 의존성 주입

@Autowired / @inject 를 통해 주입한다.

* Field 주입<br>

```java
@Component
public class MadExample {

    @Autowired
    private HelloService helloService;
}
```
가장 간단한 방법이므로 많이 쓰인다. 하지만 단점이 많다. 

* Setter 주입
```java
public class Controller {
    private Service service;

    public void setService(Service service){
        this.serviec = service;
    }
    public void callService(){
        service.doSomething();
    }
}
```
Controller 클래스의 callService()는 service에 의존하고 있다.
```java
public interface Service{
    void doSomething();
}
```
```java
public class ServiceImpl implements Service{
    @Override
    public void doSomething(){
        System.out.println("ServiceImpl is doing Something");
    }
}
```
service는 인터페이스고 인터페이스는 인스턴스화 할 수 없으므로 인터페이스의 구현체가 필요하다. ServiceImpl로 구현체를 만들어 준다.

```java
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();

        // 어떤 구현체이든, 구현체가 어떤방법으로 구현되든 Service 인터페이스를 구현하기만 하면 된다.
        controller.setService(new ServiceImpl1());
        controller.setService(new ServiceImpl2());

        controller.setService(new Service() {
            @Override
            public void doSomething() {
                System.out.println("Anonymous class is doing something");
            }
        });

        controller.setService(
          () -> System.out.println("Lambda implementation is doing something")
        );

        // 어떻게든 구현체를 주입하고 호출하면 된다.
        controller.callService();
    }
}
```
메인에서 service 인터페이스 구현체만 만들어 놓으면 controller에서는 어떤 타입의 객체라도 사용할 수 있다.(다형성) controller는 이 구현체의 내부 로직을 알 필요도 없다. **service의 구현체를 주입해주지 않아도 controller 객체는 생성가능하다. controller 객체가 생성 가능하다는 것은 callService() 호출이 가능하다는 것인데 만약 주입을 안해주고 service.callService()를 한다면 NullPointerException 에러가 난다.** <br>

❗ Setter 주입은 다소 과하다. Autowired를 setter에 붙이고 싶어서 setter 추가한 것이기 때문에(setter가 필요해서 추가한 것이 아닌) 낭비다. setter가 있기 때문에 의존성이 바뀔 수도 있다. 

* 생성자 주입
```java
public class Controller {
    private Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public void callService() {
        service.doSomething();
    }
}
```
이렇게  생성자 주입을 해주면 사용하는 쪽은 아래와 같이 바뀐다. 
```java
public class Main {
    public static void main(String[] args) {

        // Controller controller = new Controller(); // 컴파일 에러

        Controller controller1 = new Controller(new ServiceImpl());
        Controller controller2 = new Controller(
            () -> System.out.println("Lambda implementation is doing something")
        );
        Controller controller3 = new Controller(new Service() {
            @Override
            public void doSomething() {
                System.out.println("Anonymous class is doing something");
            }
        });

        controller1.callService();
        controller2.callService();
        controller3.callService();
    }
}
```
📖 이를 통해 볼 수 있는 두가지 장점
* null 을 주입하지 않는 한 NullPointerException 은 발생하지 않는다.
* 의존관계 주입을 하지 않은 경우에는 Controller 객체를 생성할 수 없다. 즉, 의존관계에 대한 내용을 외부로 노출시킴으로써 컴파일 타임에 오류를 잡아낼 수 있다.

# 2️⃣ Aspect Oriented Programming

### 🔶 AOP
> 흩어진 코드를 한 곳으로 모으는 코딩 기법

# 3️⃣ Portable Service Abstraction

### 🔶 PSA 소개
> POJO 원칙을 따른 스프링의 기능.

가령 일반적인 JUnit이나 Mybatis 등의 여러 Java framework 에서 사용가능한 라이브러리들은 spring에서 지원하는 JUnit이나 Mybatis 라이브러리와 다르다. 따라서 이러한 외부 라이브러리들은 spring에서 사용할 때 내부 구현이 달라지더라도 동일한 인터페이스로 동일한 구동이 가능하게끔 설계되어 있으며 의존성을 고려할 필요가 없다. 스프링의 대부분의 인터페이스가 PSA다.

### 🔶 스프링 트랜잭션

@Transactional 

Platform TransactionManager

@Transactional는 Platform TransactionManager라는 인터페이스를 사용해서 코딩했기 때문에 Transactional를 처리하는 aspect는 바뀌지 않는다.

### 🔶 캐시

@Cacheable / @CacheEvict

CacheManager

### 🔶 web MVC

@Controller / @RequestMapping

스프링 5부터는 Servlet를 쓰는지 Reactive를 쓰는지 몰라.

밑단의 기술을 바꾸더라도 코드를 바꿀 필요가 없어.




