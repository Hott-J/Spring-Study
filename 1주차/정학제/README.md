# Spring 

## :one: 강의소개

:white_check_mark: 어떻게 사용하는지에 초점을 맞춘다. <br>
:white_check_mark: 최신 개발 트렌드 <br>
:white_check_mark: 직접 코딩을 하며 웹앱을 만들어보며 익힌다. <br>

### :smile: 로드맵

- 스프링 입문
- 스프링 핵심 원리
- 스프링 웹 MVC
- 스프링 DB 데이터 접근 기술
- 실전! 스프링 부트

## :two: 프로젝트 환경설정

### :smile: 프로젝트 생성

- 자바 11 사용
  - 자바 1.8을 사용하고 있었는데 오류가 났음.
- 최신 인텔리제이 설치
  - 2018 인텔리제이로 했을 경우 버젼이 맞지 않아 오류가 났음.
- 스프링 부트 사용
  - start.spring.io
  - **maven**
    - 프로젝트를 진행하면서 사용할 수많은 라이브러리들을 관리해주는 도구
    - *특징적인 점은 그 라이브러리들과 연관된 라이브러리들까지 거미줄처럼 다 연동이 되어서 관리*
    - 메이븐은 네트워크를 통해서 연관된 라이브러리 까지 같이 업데이트를 해주기 때문에 과거에 일일히 수동으로 다 업데이트 혹은 연결 시켜주던 것을 자동으로 해준다는 편리함
    - 빌드를 쉽게
    - pom.xml을 이용한 정형화된 빌드 시스템
    - 뛰어난 프로젝트 정보 제공 
    - 개발 가이드 라인 제공
    - 새로운 기능을 쉽게 설치할 수 있고 업데이트할 수 있음
  - **gradle**
    - Ant처럼 유연한 범용 빌드 도구 
    - Maven을 사용할 수 있는 변환 가능 컨벤션 프레임 워크 
    - 멀티 프로젝트에 사용하기 좋음 
    - Apache Ivy에 기반한 강력한 의존성 관리 
    - Maven과 Ivy 레파지토리 완전 지원 
    - 원격 저장소나, pom, ivy 파일 없이 연결되는 의존성 관리 지원
    - 그루비 문법 사용
    - 빌드를 설명하는 풍부한 도메인 모델
    
  :white_check_mark: **maven vs gradle**
    - gradle 의 빌드 스크립트는 groovy 라는 언어로 작성해야 하므로 maven 의 xml 에 비하면 친숙하진 않지만 확장성이 뛰어나다.
    - maven 은 프로젝트가 커질수록 빌드 스크립트의 내용이 길어지고 가독성이 떨어지는 반면, gradle 은 훨씬 적은 양의 스크립트로 짧고 간결하게 작성할 수 있다.
    - maven 의 경우 멀티 프로젝트에서 특정 설정을 다른 모듈에서 사용하려면 상속을 받아야 하지만 gradle 은 설정 주입 방식으로 이를 해결한다.
    - gradle 은 멀티 프로젝트에 매우 적합하며, 빌드 속도는 다양한 시나리오 상에서 10~100배 가량이 빠르다.
  - snapshot
    - 아직 개발중인 상태
  - artifact
    - 프로젝트 명
  - **dependencies**
    - 어떤 라이브러리를 사용할 것안가
- *최신 개발 트렌드로 test 코드가 굉장히 중요하다*
- 자바 파일을 제외하고는 모두 resources 폴더안에
- **build.gradle**
  - 버전 맞추고 라이브러리를 땡겨옴
  
### :smile: 라이브러리 살펴보기

- gradle은 의존관계인 라이브러리를 전부 자동으로 가져온다
- **spring-boot-starter-web**
  - *spring-boot-starter-tomcat : 톰캣(웹서버)*
    - **자바 코드에 웹서버가 같이 올라가있다.**
  - spring-webmvc
- 실무에서는 *logging* 을 사용한다. (system.out x)
  - slf4j
  - logback
- test
  - junit : 테스트 프레임워크

### :smile: View 환경설정

  - 스프링 부트는 static에서 **index.html**을 먼저 찾는다. 없으면, **index 템플릿**을 찾는다.
  - 웹앱의 첫번째 진입점이 **컨트롤러** 이다. *@Controller*
  
  ![스프링1](https://user-images.githubusercontent.com/47052106/103139349-950a4000-471e-11eb-8384-478bf65010e8.JPG)
  
### :smile: 빌드하고 실행하기

  - 윈도우의 경우, 폴더안의 gradlew.bat / build / libs / ~.jar 를 java -jar ~.jar 로 실행한다.
  - 잘안되는 경우, `gradlew clean build` 명령어를 통해 build 폴더를 지우고, 다시 빌드한다. 

## :three: 스프링 웹 개발 기초

### :smile: 정적 컨텐츠

- 파일을 그대로 웹브라우저에 내려줌

![스프링2](https://user-images.githubusercontent.com/47052106/103139411-18c42c80-471f-11eb-81ae-9c759d3691bf.JPG)

### :smile: MVC와 템플릿 엔진

  - MVC
    - Model, View, Controller
    - View는 화면 띄움, 모델과 컨트롤러는 내부적인 처리에 집중. 모델은 화면에 필요한걸 담아서 뷰에게 전달한다.
    - 외부에서 파라미터 가져올때는 *@RequestParam*을 파라미터로 넣어준다.
    - **Ctrl + P : 파라미터 정보 출력**
    
    ![스프링3](https://user-images.githubusercontent.com/47052106/103142220-09ef7100-4743-11eb-8c3b-abb6647898b5.JPG)
    - 템플릿 엔진일 경우에는, 변환한 HTML을 웹 브라우저에게 넘겨준다. (정적과 차이점)
    - 실행
      - `http://localhost:8080/hello-mvc?name=spring`

### :smile: API

  - *@ResponseBdoy*
    - http의 body부에 내가 직접 넣어주겠다. (**return**)
    - 뷰에게 던져주지 않는다.
    - html로 변환되지 않는다.
    - 문자가 반환된 경우, 문자가 그대로 내려간다. (StringConverter)
    - 객체가 반환된 경우, JSON 방식으로 데이터를 만들어서 http 응답에 반환 (JsonConverter)
    - 요청할 때, xml로 받고 싶으면 xml로 반환해주는 converter가 동작한다.
    - 최신 트렌드는 *JSON*
  - API
    - 자바 class 안에 static class1을 정의할 경우, class.class1로 호출 가능
    - **Alt + Insert : Generator**
    - *Key : Value 형태의 JSON 으로 반환된다*
    - Getter & Setter 는 자바 빈 규약이다.
      - public 메서드를 통해 private 변수를 접근하게끔 한다.
        - property 접근 방식
    - 객체를 JSON으로 바꾸기 위해 Jackson, Gson 라이브러리를 사용한다.
      - 스프링은 Jackson
      
    ![스프링4](https://user-images.githubusercontent.com/47052106/103142325-c0078a80-4744-11eb-9d14-b7249a402d01.JPG)

## :three: 회원 관리 예제 - 백엔드 개발

### :smile: 비지니스 요구사항 정리

  ![스프링1](https://user-images.githubusercontent.com/47052106/103149129-004d2400-47aa-11eb-8f06-2a4fac8ed132.JPG)

### :smile: 회원 도메인과 리포지토리 만들기

- *Optional*
  - 자바 8에 들어가있는 기능
  - NULL 값이 반환될 수 있으므로, 이를 Optional로 감싸서 반환
  - Optional.ofNullable로 감싸면, 해당값이 NULL이여도 클라이언트에서 처리를 할 수 있음
- **Alt + Enter 로 인터페이스 상속하는 메소드 보이게끔 가능**
- **MAP**
  - *Key : Value* 로 이루어짐
  - put()
    - 입력
  - get()
    - key에 해당되는 값을 얻음
  - remove()
    - 맵(Map)의 항목을 삭제하는 메소드로 key값에 해당되는 아이템(key, value)을 삭제한 후 그 value 값을 리턴
  - *Stream()* 
  ```java
  ArrayList<string> list = new ArrayList<>(Arrays.asList("Apple","Banana","Melon","Grape","Strawberry"));

  System.out.println(list);

  //[Apple, Banana, Melon, Grape, Strawberry]
  ```
    - map
      - 요소들을 특정 조건에 해당하는 값으로 변환
      ```java
      System.out.println(list.stream().map(s->s.toUpperCase()).collect(Collectors.joining(" "))); //APPLE BANANA MELON GRAPE STRAWBERRY
      System.out.println(list.stream().map(s->s.toUpperCase()).collect(Collectors.toList())); //[APPLE, BANANA, MELON, GRAPE, STRAWBERRY]
      list.stream().map(String::toUpperCase).forEach(s -> System.out.println(s));
      //APPLE
      //BANANA
      //MELON
      //GRAPE
      //STRAWBERRY
      ```
      - Collectors.joining 을 이용해 리스트를 조인의 기준으로 배치 할 수 있습니다. String 으로 리턴합니다.
      - Collectors.toList 를 이용해 리스트로 리턴 받을 수 있습니다.
      - forEach 요소마다 각각 작업을 할 수 있습니다.
    - filter
      - 요소들을 조건에 따라 걸러내는 작업
      ```java
      System.out.println(list.stream().filter(t->t.length()>5).collect(Collectors.joining(" "))); //Banana Strawberry
      System.out.println(list.stream().filter(t->t.length()>5).collect(Collectors.toList())); //[Banana, Strawberry]
      ```
    - sorted
      - 요소들을 정렬
      ```java
      System.out.println(list.stream().sorted().collect(Collectors.toList())); //[Apple, Banana, Grape, Melon, Strawberry] 
      ```
  - collect()
    - collect는 stream의 데이터를 변형 등의 처리를 하고 원하는 자료형으로 변환해 줍니다.
    `collect(Supplier supplier, BiConsumer accumulator, BiConsumer combiner)`
    - collect에 3개의 params을 넣어야 해서 다소 사용하기 번거롭습니다. Collectors라는 라이브러리가 기본적인 기능들을 제공해주어 이런 불편함을 해결
    - toSet()
    `Set<String> fruitSet = fruits.collect(Collectors.toSet()); //Set 자료형으로 변환`
    - toList()
    `List<String> fruitList = fruits.collect(Collectors.toList()); //List 자료형으로 변환`
    - joining()
    `String result2 = fruits.collect(Collectors.joining()); //String 자료형으로 변환`
    `String result2 = fruits.map(Object::toString).collect(Collectors.joining(", ")); // , 로 구분자 넣어서 구분 용이`
    - toMap()
      - Custom 객체에 대해서 Collect
     ```Java
      Stream<Fruit> fruits2 = Stream.of(new Fruit("1", "banana"), new Fruit("2", "apple"),
        new Fruit("3", "mango"), new Fruit("4", "kiwi"),
        new Fruit("5", "peach"), new Fruit("6", "cherry"),
        new Fruit("7", "lemon"));
      Map<String, String> map = fruits2.collect(Collectors.toMap(Fruit::getId, Fruit::getName));
      for (String key : map.keySet()) {
        System.out.println("key: " + key + ", value: " + map.get(key));
      }

      static class Fruit {
        public String id;
        public String name;

        Fruit(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
          return id;
        }
        public String getName() {
          return name;
        }
    }
    ```
      - 동일한 Key에 대한 예외처리
      ```Java
      Collectors.toMap(item -> item.getId(), item -> item.getName(),
            (existingValue, newValue) -> existingValue)); // 처음에 입력한 값 출력
      ```
      ```Java
      Collectors.toMap(item -> item.getId(), item -> item.getName(),
            (existingValue, newValue) -> {
                    String concat = existingValue + ", " + newValue;
                    return concat;
                })); // 처음 입력한 값 + 이후에 입력한 값 출력
      ```
