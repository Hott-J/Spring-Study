### :smile: 순환 참조

#### :book: 순환 참조란?
* Bean A도 Bean B를 참조하고, Bean B도 Bean A를 참조하는 경우에 발생
* Bean B가 생성되지 않았는데 Bean A가 Bean B를 참조해 버리면 문제 발생
* 따라서 순환 참조가 발생하면 스프링은 Bean A, B 둘중 어느 것을 먼저 생성해야 하는지 결정하지 못하고, 결국 오류를 발생시킨다.

#### :book: 순환 참조 해결 방법
* @Lazy 어노테이션 사용
  ```java
  @Component
  public class A {
      private final B b;
      
      @Autowired
      public A(@Lazy B b) {
          this.b = b;
      }
  }
  ```
  ```java
  @Component
  public class B {
      private final A a;
      
      @Autowired
      public B(A a) {
          this.a = a;
      }
  }
  ```
  * @Lazy 방식은 Bean A의 초기화 시점에 Proxy Bean B를 주입하여 초기화를 미루고, 실제로 Bean A가 사용될 때 Bean B를 주입하는 방식이다.
  * 스프링에서 권장하지 않는 방식이다.

* Setter 주입, Field 주입
  * 순환 참조를 회피할 수 있다. 그러나 요즘에는 생성자 주입을 권장하므로, 잘 안쓰이는 방법이다.
  * 생성자 주입은 순환 참조가 일어났을 경우 에러를 발생시키면서 Spring이 구동되지 않기 때문에 순환 참조에 대한 예방이 가능하다.
  
* 순환 참조 에러 해결 방법이 여러개 있지만, 순환 참조가 안 일어나도록 설계하는 것이 좋은 디자인이다.
