## :cherry_blossom: 회원 관리 예제 - 웹 MVC 개발

### 1. 회원 웹 기능 - 홈 화면 추가
* HomeController 추가
```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";  // templates 폴더 내에 있는 home.html 을 찾는다.
    }
}
```
* static/index.html(Spring boot가 제공하는 Welcome page 기능)과 templates/home.html("/" Mapping) 중 무엇이 더 우선순위가 높을까?
  * 우선, HTTP 요청이 오면 Spring Container 내에 있는 관련 Controller가 있는지 찾는다. 없으면 그때 static file을 반환한다.
  * HTTP 요청으로 맨 처음 "/"가 오는데, "/"에 mapping되는 Controller가 있으면 templates 내에 있는 html 파일을 리턴한다. 이렇게 되면 static file은 무시된다.
  * @GetMapping("/")을 처리하는 Controller를 지우면 다시 static file이 등장하게 될 것이다.
  * 결국, Controller가 정적 파일보다 우선순위가 높다고 할 수 있다!

### 2. 회원 웹 기능 - 등록
* 회원 등록 폼 Controller

* 웹 등록 화면에서 데이터를 전달 받을 폼 객체

* 회원 Controller에서 회원을 실제 등록하는 기능


### 3. 회원 웹 기능 - 조회
