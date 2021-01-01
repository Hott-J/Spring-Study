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
-  **빈(bean)**을 만들고 엮어주며 제공해준다.
  - 빈은 @Component , @Service, @Repository, @Controller 와 같은 애노테이션을 보고 알 수 있다.
    - 컴포넌트 스캔
 
