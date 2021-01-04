### 1️⃣ 회원 관리 - 웹 MVC 개발
* 회원 웹 기능 - 홈 화면 추가
* 회원 웹 기능 - 등록
* 회원 웹 기능 - 조회
#### 🔶 회원 웹 기능 - 홈 화면 추가
```
'localhost:8080/' 을 입력했을 때
```
```java
@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
```
"/"에 해당하는 controller가 존재한다면<br>
✔ resources > templates > home.html을 반환한다. 
```java
@Controller
public class HomeController {
    @GetMapping("/qwer")
    public String home(){
        return "home";
    }
}
```
"/qwer"에 해당하는 controller가 없다면<br>
✔ resources > static > index.html을 반환한다. (정적 컨텐츠 반환)
#### 🔶 회원 웹 기능 - 홈 화면 추가

