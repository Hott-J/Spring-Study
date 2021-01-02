# 로그 사용하기

# 로그 사용하기

### 로깅이란?

프로그램 개발 중이나 완료 후 발생할 수 있는 **오류에 대해 디버깅**하거나 운영중인 **프로그램의 상태를 모니터링** 하기 위해 필요한 **정보를** **기록**하는 것

- 프로그램 실행 추적을 콘솔, 파일, DB에 작성/기록함
    - Java의 주요 Logging Framework
    - native java.util.logging: 별로 사용 X
    - Log4J: 몇 년 전까지 사실상 표준
    - Logback: Log4J 개발자가 만든 Log4J의 후속 버전, 현재 많은 프로젝트에서 사용
    - SLF4J(Simple Logging Facade for Java): Log4J 또는 Logback과 같은 백엔드 `Logger Framework`의 `facade pattern`
    tinylog: 사용하기 쉽게 최적화된 Java용 최소형(75KB Jar) 프레임워크

- 장점
    - 로그 출력은 나중에 살펴볼 수 있도록 영구 매체에 저장 가능
    - 우선순위 level로 나눠 (trace, debug, info, warn, error) 출력 가능
    - 모든 모듈 또는 특정 모듈 또는 클래스에 대해 메시지를 출력할 수 있다.
    - 메시지 형식을 지정할 수 있다.

### slf4j

slf4j는 자바 로깅을 쉽게 사용할 수 있도록 하는 라이브러리 **(facade pattern)**

![%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5%20bc417f4d13184b3bbaf4ae86f5bd1fc4/_2021-01-02__1.33.42.png](%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5%20bc417f4d13184b3bbaf4ae86f5bd1fc4/_2021-01-02__1.33.42.png)

- 여러 개의 클래스가 하나의 역할을 수행할 때, 대표적인 인터페이스만을 다루는 클래스를 두어 원하는 기능을 처리할수 있게 도와주는 패턴
- slf4j는 로그를 남기기 위한 공통 `인터페이스 역할`을 한다.
    - 구현체와 상관없는 로깅 코드 구현!!
- 프로젝트의 로깅 시스템을 변경하고 싶을 때 maven 또는 gradle의 의존성 설정만 바꾸면 됨
- ex) log4j, logback

### log4j2 로깅 시스템 사용

**프로젝트 구조**

- log4j2의 설정파일을 찾아서 설정을 적용

```
+---src
|   +---main
|   |   +---java
|   |   |   +---log4j2
|   |   |   |       Log4j2.java
|   |   |
|   |   \---resources
|   |           log4j2.xml
+---pom.xml     
```

**의존성 관리**

- log4j를 위한 의존성 (maven) → pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.10.0</version>
    </dependency>
</dependencies>
```

**소스 코드**

- 테스트를 통한 로그 실행

```java
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final Calculator calculator = new Calculator();
    //private final static Logger LOGGER = Logger.getGlobal();
    private static Logger LOGGER = LoggerFactory.getLogger(CalculatorTest.class);

    @Test
    void Addition() {
        int a = 3;
        int b = 5;
        if (Math.addExact(a, b) == calculator.Add(a, b)) {
            System.out.println("Right Addition Calculate");
            LOGGER.debug("Right Addition Calculate");
        } else {
            System.out.println("Wrong Addition Calculate");
            LOGGER.error("Wrong Addition Calculate");
        }
        assertEquals(Math.addExact(a, b), calculator.Add(a, b));
    }

    @Test
    void Substitution() {
        int a = 3;
        int b = 5;
        if (Math.subtractExact(a, b) + 11231321 == calculator.Sub(a, b)) {
            System.out.println("Right Substitution Calculate");
            LOGGER.info("Right Substitution Calculate");
        } else {
            System.out.println("Wrong Substitution Calculate");
            LOGGER.error("Wrong Substitution Calculate");
        }
        assertEquals(Math.subtractExact(a, b), calculator.Sub(a, b));
    }
}
```

- 실행 결과
    - println은 문자 정보만 찍혀서 나옴
    - log는 날짜 정보와 사용자가 설정한 정보가 추가로 얻을 수 있음

![%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5%20bc417f4d13184b3bbaf4ae86f5bd1fc4/_2021-01-02__2.39.22.png](%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5%20bc417f4d13184b3bbaf4ae86f5bd1fc4/_2021-01-02__2.39.22.png)

- 만약 `log4j2`를 사용하지 않고 `logback`과 같은 다른 로깅 시스템으로 바꾸고 싶다면 `pom.xml` 수정만으로 바꿀 수 있음

```xml
<!--        log4j2-->
<!--        <dependency>-->
<!--            <groupId>org.apache.logging.log4j</groupId>-->
<!--            <artifactId>log4j-slf4j-impl</artifactId>-->
<!--            <version>2.10.0</version>-->
<!--        </dependency>-->

<!--    logback-->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
```

**log4j2.xml (log4j2 설정 Configuration 파일)**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="INFO">
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="debug" additivity="false">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
```

- log4j2 설정파일은 properties, json, yml, xml 등의 파일 확장자를 가짐
- log4j2가 설정파일을 찾는 메커니즘은 classpath를 기준으로 이루어짐 만일 설정 파일을 찾지 못하면 default 설정 적용
- Configuration 태그의 status는 log4j2 내부적으로 어떻게 동작되는 지 볼 수 있는 로그 수준을 결정하는 속성
- status를 INFO로 했을 경우에는 log4j2가 내부적으로 어떻게 동작하는 지 유의미한 로그를 보기 어렵지만 TRACE나 DEBUG로 놓았을 시에는 손쉽게 내부 로그를 볼 수 있다.
- **Configuration status="TRACE" 로 놓았을 시 콘솔 로그**

```
2019-05-27 16:05:16,601 main DEBUG Initializing configuration XmlConfiguration[location=D:\JavaProject\target\classes\log4j2.xml]
2019-05-27 16:05:16,608 main DEBUG Installed 1 script engine
Warning: Nashorn engine is planned to be removed from a future JDK release
2019-05-27 16:05:16,858 main DEBUG Oracle Nashorn version: 11.0.1, language: ECMAScript, threading: Not Thread Safe, compile: true, names: [nashorn, Nashorn, js, JS, JavaScript, javascript, ECMAScript, ecmascript], factory class: jdk.nashorn.api.scripting.NashornScriptEngineFactory
```

- Appenders 태그는 로그를 어떻게 출력할 것인지를 결정하는 정의하는 태그 (파일, 콘솔, DB에 쓰기 가능) 
`<Console name="console" target="SYSTEM_OUT">` ⇒ 콘솔 출력 또한 PatternLayout 자식 태그 ⇒ 로그 패턴 지정
- Loggers 에서는 각 Logger 들에 대한 설정을 정의할 수 있습니다.
    - Root는 logger 들의 최상위 부모 logger 로서 만일 하위 logger들에 대한 설정이 없다면 이 Root 로거의 설정 정보를 자동적으로 상속받아 쓰게 되어 있습니다. 또한 Logger의 하위 태그인 AppendRef를 지정하여 어떤 Appender를 쓸 지 지정할 수 있음
