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
- 그 외는 조사
  
### :two: IOC 컨테이너
- 빈을 만들고 엮어주고 제공한다
- application context로 직접 만들어줄 수 있다?
- 여러가지를 상속한다.
- getBean으로 꺼낼수있다.-> 요즘은 직접 쓰지 않는다.
### :three: 빈
- 빈을 구분하는 방법: IntelliJ에서 옆에 초록 원이 뜨면 빈이다.
- java로 직접 등록 -@Bean으로 @Configuration안에서만 가능
- 꺼내쓰는법 :ApplicationContext안에서 뺴주는 것을 으미

### :four: 의존성 주입
- 생성자가 하나만있으면 autowired가 없더라도 있음
- 왠만하면 생성자로 사용하기.

#### :smile: PetRepository가 스프링 데이터 JPA에서 빈으로 등록된다고?
#### :smile: VisitController에서 두가지를 연결?

### :five: AOP
- byte code를 사용하는 방법과 프록시 패턴을 사용하는 방법
- transactional과의 관계? 어디든 transactional이 있다?
### :six: PSA
- portable service application
- spring이 제공하는 거의 대부분
- psa를 사용해 코드를 짜야 Test case 만들기도 쉽고 확장하기가 쉬움

### :seven: 스프링 트랜잭션
ASPECT느낌임



