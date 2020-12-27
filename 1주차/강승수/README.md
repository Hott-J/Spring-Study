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
- 동작 구조
![정적](https://user-images.githubusercontent.com/51367515/103170842-d1f34580-488a-11eb-9502-b7cf208b50ce.PNG)

## MVC
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

-위와 같이 
    
