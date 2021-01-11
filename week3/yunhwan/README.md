# 백기선님 스프링 강의 정리
> PetClinic 예제
## IoC (Inversion of Control)

코드  왼쪽에 콩이 있으면 **`빈`** 이다.

- IoC 컨테이너
    - 빈으로 등록된 것들은 **IoC 컨테이너**가 객체를 만들고 객체의 의존성을 엮어서 관리한다.

# DI란?

### 객체 의존성

- 현재 객체가 다른 객체와 상호작용(참조)하고 있다면 현재 객체는 다른 객체에 `의존성`을 가진다.

```java
public class PetOwner{
    private AnimalType animal;

    public PetOwner() {
        this.animal = new Dog();
    }
}
//https://gmlwjd9405.github.io/2018/11/09/dependency-injection.html
```

이런 식으로 PetOwner객체와 AnimalType간에 강한 결합이 생긴다.

- 하나의 모듈 수정 시 이를 의존하는 다른 모듈까지 변경 되어야 한다.
- 두 객체 사이에 의존성 존재 시 Unit Test 작성이 어렵다.

### Dependency Injection(의존성 주입)

> 객체 자체가 아닌 Framework에 의해 객체의 의존성이 주입되는 설계 패턴

- 프레임워크에 의해 의존성이 동적으로 주입되기 때문에 객체 간의 결합이 줄어듬

    → 스프링 컨테이너가 bean객체를 생성하고 의존성 주입을 수행

- 만약, DI를 사용하지 않는다면 다른 객체를 사용하기 위해선 직접 코드를 수정해줘야 한다.
- DI와 IOC는 같은 의미이며 IOC가 DI에 의해 달성됨
- IOC(Inversion Of Control) : 프로그램의 제어권을 framework가 가지는 것
    - 개발자는 설정(xml, annotation)만 하면 Container가 알아서 처리
    - 우리는 프레임워크 안에서 코딩만하면 됨

### DI의 장점

1. 종속성 감소 : 컴포넌트간 종속성 감소로 변경이 쉬워진다.
2. 재사용성 증가 : 다른 구현이 필요한 경우 코드 변경없이 해당 구현을 사용하도록 쉽게 변경할 수 있다.
3. 더 많은 테스트 코드 작성
4. 코드 가독성

## Spring Container

> 스프링의 핵심 컨테이너

- 컨테이너는 DI를 사용해 bean객체를 관리한다.
    1. 객체(bean) 생성
    2. 객체를 묶어 구성하기
    3. 객체들의 전체 수명주기(lifecycle)을 관리하기
