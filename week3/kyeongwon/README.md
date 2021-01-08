## :cherry_blossom: Spring PetClinic 예제

### 1. 프로젝트 세팅
* Java 1.8 설치

* 소스 코드 : [https://github.com/spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic) - 간단한 동물병원 예제

* 소스 코드 다운로드
  ```
  git clone https://github.com/spring-projects/spring-petclinic.git
  cd spring-petclinic
  ```
  * 다운로드 후 IntelliJ에서 spring-petclinic open
  
* 실행 방법
  * spring-boot:run 실행
  * PetClinicApplication 실행
  * 만약 frontend 쪽을 수정했다면, Maven(spring-boot:run이나 wro4j:run)으로 실행해 주어야 한다.
  
### 2. PetClinic 프로젝트 살펴보기
* CRUD 프로젝트
<br/>

## :cherry_blossom: IoC

### 1. IoC(Inversion of Control)
* 원래 의존성에 대한 제어는 개발자에게 있었다.
  ```java
  class OwnerController {
      private OwnerRepository repository = new OwnerRepository();
  }
  ```
  
* 개발자가 의존성을 설정하지 않고 외부에서 의존성 주입을 해주도록 했다. -> **IoC**
  ```java
  class OwnerController {
      private OwnerRepository repo;
      public OwnerController(OwnerRepository repo) {
          this.repo = repo;
      }
      
      // 이후 repo를 사용합니다.
  }
  
  class OwnerControllerTest {
      @Test
      public void create() {
          OwnerRepository repo = new OwnerRepository();
          OwnerController controller = new OwnerController(repo); // OwnerRepository를 OwnerController에게 주입
      }
  }
  ```
  
* Servlet에 대한 제어권도 IoC라고 볼 수 있다. 
  * Servlet Container : 클라이언트 요청을 처리하는 컨테이너(ex-톰캣(Tomcat))
  
### 2. IoC Container
* 객체의 생성과 생명주기를 관리하는 역할
* IoC Container내부에 생성된 OwnerController, OwnerRepository같은 Bean들의 의존성을 관리한다.
* @Component, @Controller, @Service, @Repository가 붙어있는 객체들만 Bean으로 등록된다.

### 3. Bean
* 스프링 IoC 컨테이너가 관리하는 객체 (OwnerController, OwnerRepository는 Bean이지만 Owner, Pet은 Bean이 아니다.)
* Bean 등록 방법
  * Component Scanning
    * @Component, @Controller, @Service, @Repository 어노테이션이 있다.
    * 메인 class에 @SpringBootApplication이 있는데 이 안에 @ComponentScan이 존재한다.
    * 모든 package에 @Component가 있는 class를 찾아서 Bean으로 등록해준다. (@Controller, @Service, @Repository 내부에 @Component가 존재한다.)
  * XML이나 자바 설정 파일에 등록
    * 직접 @Bean을 붙여서 등록(단, @Configuration이 붙은 class 내부여야 한다. @SpringBootApplication은 @Configuration도 포함하고 있다.)

### 4. 의존성 주입(DI)
* [1주차 DI 정리](https://github.com/Hott-J/Spring-Study/tree/main/week1) 참고
* 의존성 주입을 할 때 주로 @Autowired, @Inject를 붙여서 사용한다.
<br/>

## :cherry_blossom: AOP

### 1. AOP
* 흩어져 있는 코드들을 한 곳으로 모아서 실행시킨다.

* Single Responsible Principle를 보장해준다. - 한 클래스는 하나의 일만을 한다.

  * 원래 코드
  ```java
  class A {
      method a () {
          AAAA
          Hello
          BBBB
      }
      method b () {
          AAAA
          Java
          BBBB
      }
  }
  
  class B {
      method c() {
          AAAA
          Spring
          BBBB
      }
  }
  ```
  
  * AOP 코드
  ```java
  class A {
      method a () {
          Hello
      }
      method b () {
          Java
      }
  }
  
  class B {
      method c() {
          Spring
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
* 이외에도 Byte code로 조작하는 방법, Proxy pattern을 사용하는 방법이 있다.

* Spring AOP는 Proxy pattern을 사용한다.

### 2. AOP 적용 예제
* 특정 annotation이 있는 method만 시간을 재서 log에 기록

* @LogExecutionTime
  ```java
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface LogExecutionTime {
  }
  ```
  
* LogAspect Bean : @LogExecutionTime이 붙은 곳만 적용
  ```java
  @Component
  @Aspect
  public class LogAspect {
      Logger logger = LoggerFactory. getLogger (LogAspect. class);
      
      @Around ("@annotation(LogExecutionTime)")
      public Object logExecutionTime (ProceedingJoinPoint joinPoint) throws Throwable {
          StopWatch stopWatch = new StopWatch();
          stopWatch.start() ;
          Object proceed = joinPoint.proceed();
          stopWatch.stop() ;
          logger .info(stopWatch.prettyPrint());
          return proceed ;
      }
  }
  ```
<br/>
  
## :cherry_blossom: PSA

### 1. PSA(Portable Service Abstraction)
* 스프링 내부적으로 서블릿 기반으로 코드가 동작하지만 서블릿 기술은 추상화 계층에 의해 숨겨져 있는 것이다.

### 2. 스프링 Transaction
* @Transactional 처리하는 코드는 바뀌지 않는다. -> 추상화의 장점
* PlatformTransactionManager : JpaTransacionManager, DatasourceTransactionManager, HibernateTransactionManager 등..
* PetClinic 예제는 스프링 데이터 JPA를 쓰기 때문에, spring boot에 의해 **JPA Transaction manager**가 Bean으로 자동 등록된 상태이다.

### 3. 캐시
* @EnableCaching : 캐시 기능 활성화 -> @Cacheable, @CacheEvict 사용할 수 있다!
* 캐시도 Transaction과 마찬가지로 @Cacheable, @CacheEvict를 처리할 Aspect가 필요하기 때문에 **CacheManager**가 등록되어 있다.
* CacheManager : JCacheManager, ConcurrentMapCacheManager, EhCacheCacheManager 등..

### 4. 스프링 웹 MVC
* @Controller, @GetMapping과 같은 어노테이션만으로는 Servlet인지, Reactive인지 알 수 없다. -> 추상화
