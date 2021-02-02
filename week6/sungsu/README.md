# :cherries: 처음 시작하는 스프링 부트2

## :tulip: 스프링 부트 환경 설정

### 프로젝트 환경
- 빌드 도구: Gradle Project
- 언어 : java8
- version : 2.4.2
- IntelliJ 커스텀 -> 스프링 이니셜라이저를 사용해서 생성
### gradle
- gradle 버전 : 6.7.1 -> 본문에서는 4.8.1 버전을 권장했으나 spring boot 2.4.2 버전에서 이를 지원하지 않기에 6.7.1version으로 진행하였다.

### @Value vs @ConfigurationProperties

- 둘다 yaml이나 properties 에 있는 값을 매핑시키는 annotation
- @Configuration이 유연한 바인딩과 메타데이터를 지원한다.
- @Value("${property}") 주석을 사용하여 구성 속성을 주입하는 것은 번거로울 수 있으며, 특히 여러 속성을 사용 중이거나 데이터가 계층적일 경우 스프링 부트는 다음 예와 같이 강하게 입력된 Bean 이 응용 프로그램의 구성을 제어하고 검증할 수 있도록 하는 대체적인 속성 작업 방법을 제공한다.
 

- :smile: 결론은, 호출하여 사용하려는 Properties 정보가 여러 개 일 경우에는 Bean 으로 등록하여 사용하는 @ConfigurationProperties 가 좋지만 단일 정보만을 사용한다면 사용법이 더 간편하고 간단한 @Value 를 사용해도 무방하다.

- getList오류 왜나는 것일까?

### 자동 환경 설정
- @SpringBootApplication=@SpringbootConfiguration+@EnableAutoConfiguration+@CompontScan
## :tulip: 스프링 부트 테스트
### SpringBootTest
- :smile: @RunWith(SpringRunner.class0): @RunWith는 JUnit 의 기본 러너 대신에 사용할 러너를 지정해주는 것. JUnit 프레임워크가 테스트를 실행할 시(JUnit은 내장된 Runner를 테스트 시 실행하고 됨) 테스트 실행방법을 확장할 때 쓰는 어노테이션 이다. 쉽게 말해 JUnit 프레임워크가 내장된 Runner를 실행할 때 @Runwith 어노테이션을 통해 SpringRunner.class라는 확장된 클래스를 실행하라고 지시한 것.

- @SprinBootTest annotation을 사용할 때 실행한 예제 3-2를 실행시켰더니 오류가 났다. value와 properties 둘 중 한가지만 사용하여야 한다고 해서 properties를 제거해서 실행해보았지만 다른 오류가 발생하였다. gradle version을 4.12로 올리고 spring boot version을 2.2.6으로 올려서 해결하였다.

### WebMvcTest
- @NoArgsController : 기본 생성자를 만들어주는 어노테이션

- Service는 인터페이스만 생성함. 구현체는 MOCK로 만듬 -> url로 입장 불가. 하지만 코드를 통해 실행실 수 있음.

### DataJpaTest

- @DataJpaTest는 사용한 데이터를 롤백함.@Transactional이랑 비슷함.

- Book class를 JPA 관련 어노테이션을 추가하여 test를 진행함.

- Test에서 Book class 변수에 맞는 값을 매핑시켜서 빌드 가능함. 이를 통해 여러가지 메소드를 테스트해 볼 수 있다.

### RestClientTest

- Mock방식을 사용함.

- json파일을 사용하여 Rest가 잘 이뤄지는 지 테스트.

### JsonTest

- 문자열을 json객체로 변환시킨 뒤 테스트

- json을 문자열로 변환 후 테스트