# SPRING 1주차

## :one: 스프링이란?
- 스프링 프레임워크는 자바 플랫폼을 위한 오픈 소스 애플리케이션 프레임워크로서 간단히 스프링이라고도 한다. 동적인 웹 사이트를 개발하기 위한 여러 가지 서비스를 제공하고 있다.

## :two: 스프링 설치

- 프로젝트 환경
    - JAVA: JAVA 11
    - IDE: IntelliJ
    - 사용 프로젝트: gradle project
    - Dependancies: Spring Web, Thymeleaf

## :three: 라이브러리
- 외부 라이브러리
    - ex)TOMCAT, JUNIT, SPRING BOOT
    - 각각의 의존 관게를 지니고 있음

### :smile: 출력 사항에는 console.log를 사용하기!


## :four: 환경설정
- resources/static/index.html로 파일을 설정해 놓으면 welcome page로 인식함. index.html을 못 찾을 경우 index 템플릿을 찾는다.
```
<!DOCTYPE HTML>

<html>
    <head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    </head>
<body>
    Hello
    <a href="/hello">hello</a>
</body>
</html>

```

- 정적 파일이 아닌 동적 파일을 만들어주기위해서 thymeleaf 라이브러리르 이용한다. 밑의 코드는 정적인 html 파일에 변수 출력이 가능함을 보여준다.

```//HelloController.java


package hello.hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

}
```
```
//hello.html
<!DOCTYPE HTML>

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
Hello
<p th:text="'안녕하세요. '+ ${data}">안녕하세요. 손님</p>
</body>
</html>
```
- 동작 
![캡처](https://user-images.githubusercontent.com/51367515/103169339-06143980-487e-11eb-850d-39c233fd0a32.PNG)

- hello로 입장과 동시에 HelloController로 이동

## :five: 정적 컨텐츠
![정적](https://user-images.githubusercontent.com/51367515/103170842-d1f34580-488a-11eb-9502-b7cf208b50ce.PNG)
- 위와 같이 정적 url에 입력된 정적파일을 실행시켜줌을 볼 수 잇다.

## :six: MVC
- Model, View, Controller
    
- controller code
    ```@GetMapping("hello-mvc")
    public String helloMvc( @RequestParam("name") String name, Model model){
        model.addAttribute("name",name);
        return "hello-template";
    }
    ```
- html code(hello-template)
    ```<!DOCTYPE HTML>

	<html xmlns:th="http://www.thymeleaf.org">

	<body>
	Hello
	<p th:text="'hello. '+ ${name}">hello! empty</p>
	</body>
	</html>
    ```

![mvc](https://user-images.githubusercontent.com/51367515/103170882-27c7ed80-488b-11eb-9ddc-7346c97f4c3e.PNG)

- url에 입력된 값을 parameter로 입력받아서 html에 출력시켜줌을 볼 수 있다.

## :seven: API
- ResponseBody 문자 반환
    - html 파일을 보내는 것이 아닌 문자열 자체를 보냄
```
@GetMapping("hello-string")
 @ResponseBody
 public String helloString(@RequestParam("name") String name) {
 return "hello " + name;
}
```

- json 형식으로 전달하기
```
@GetMapping("hello-api")
 @ResponseBody
 public Hello helloApi(@RequestParam("name") String name) {
 Hello hello = new Hello();
 hello.setName(name);
 return hello;
 }
 static class Hello {
 private String name;
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
}
```


### :smile: json이란?
- JSON은 속성-값 쌍( attribute–value pairs and array data types (or any other serializable value)) 또는 "키-값 쌍"으로 이루어진 데이터 오브젝트를 전달하기 위해 인간이 읽을 수 있는 텍스트를 사용하는 개방형 표준 포맷
- 요즘은 xml보다 json으로 반환하는 것이 기본!    


![reqbody](https://user-images.githubusercontent.com/51367515/103171499-2fd65c00-4890-11eb-82fe-68b67c79a32d.PNG)

- json을 {name:spring}형태로 바꾸어서 응답


## :eight: 회원관리 예제

### :smile: 비즈니스 요구사항
- 데이터: 회원 ID, 이름
- 기능: 회원 등록, 조회
- DB
![웹애플리케이션](https://user-images.githubusercontent.com/51367515/103205765-af6e3480-493d-11eb-994f-eaceaa82452e.PNG)
- 컨트롤러
- 서비스
- 리포지토리
- 도메인
 
- test code
```
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMemberRepositoryTest {

    MemberRepository repository= new MemoryMemberRepository();

    @Test
    public void save(){
        Member member = new  Member();
        member.setName("spring");

        repository.save(member);

        Member result=repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2= new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result= repository.findByName("spring1").get();

        assertThat(result).isEqualTo(result);

    }

    @Test
    public void findAll(){
        Member member1= new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2= new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);

    }
}

```
- AfterEach란 : 한 번에 실행시키면 전에 했던 내용이 변수에 남아 있을수 있으므로 이를 초기화 해주는 작업이 필요함

- 회원 서비스 개발
```
package hello.hellospring.service;
import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
public class MemberService {
 private final MemberRepository memberRepository = new
MemoryMemberRepository();
 /**
 * 회원가입
 */
 public Long join(Member member) {
 validateDuplicateMember(member); //중복 회원 검증
 memberRepository.save(member);
 return member.getId();
 }
 private void validateDuplicateMember(Member member) {
 memberRepository.findByName(member.getName())
 .ifPresent(m -> {
 throw new IllegalStateException("이미 존재하는 회원입니다.");
 });
 }
 /**
 * 전체 회원 조회
 */
 public List<Member> findMembers() {
 return memberRepository.findAll();
 }
 public Optional<Member> findOne(Long memberId) {
 return memberRepository.findById(memberId);
 }
}
 ```
 


    
