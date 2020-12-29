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

- 스프링 싱글톤은 클래스 자체에 의해서가 아니라, **스프링 컨테이너(Bean Factory/Application Context)**에 의해 구현된다.
- 스프링에서는, 컨테이너 내에서 특정 클래스에 대해 @Bean이 정의되면, *스프링 컨테이너는 그 클래스에 대해 딱 한 개의 인스턴스를 만든다.* 이 공유 인스턴스는 설정 정보에 의해 관리되고, **bean이 호출될 때마다 스프링은 생성된 공유 인스턴스를 리턴 시킨다.(중요)** 
- 공유인스턴스의 생성시점은 스프링 컨테이너에 따라 달라지는데, 빈팩토리는 최초 호출시점에서 인스턴스를 생성하고(Lazy loading), 애플리케이션 컨텍스트는 미리 모든 공유인스턴스를 다 초기화해 두었다가 호출될때 바로 리턴시켜준다. 
- bean 관리의 주체인 스프링 컨테이너는 그 어떤 호출에도 **단일 공유 인스턴스를 리턴**시키므로 *thread safety도 자동으로 보장된다.*

![img](https://user-images.githubusercontent.com/47052106/103311192-09b1e700-4a5d-11eb-8245-576441491e72.png)
