
# :cherries: 회원 관리 예제 - 웹 MVC 개발

## :tulip: 홈 화면 추가
- 맨 처음 홈페이지에 들어왔을 때의 메인 화면을 담당

### 홈 컨트롤러 추가

```java
package hello.hellospring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
 @GetMapping("/")
 public String home() {
 return "home";
 }
}
```


### html 파일 추가
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
 <div>
 <h1>Hello Spring</h1>
 <p>회원 기능</p>
 <p>
 <a href="/members/new">회원 가입</a>
 <a href="/members">회원 목록</a>
 </p>
 </div>
</div> <!-- /container -->
</body>
</html>
```

#### :smile: 만약 @GetMapping("/")된 값이 없으면 index.html이 먼저 실행된다. 하지만 있다면 index.html은 우선순위에서 밀림!

## :tulip: 등록 기능 추가

- form이라는 형식으로 html에서 값을 받아옴
### 웹 등록 화면에서 데이터를 전달 받을 폼 객체
```java
package hello.hellospring.controller;
public class MemberForm {
 private String name;
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
}
```

### 회원 컨트롤러에서 회원을 실제 등록하는 기능
```java
@PostMapping(value = "/members/new")
public String create(MemberForm form) {
 Member member = new Member();
 member.setName(form.getName());
 memberService.join(member);
 return "redirect:/";
}
```

## :tulip: 조회 기능 추가

### 회원 컨트롤러에서 조회 기능
```java
@GetMapping(value = "/members")
public String list(Model model) {
 List<Member> members = memberService.findMembers();
 model.addAttribute("members", members);
 return "members/memberList";
}
```
### 회원 리스트 HTML
```<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
 <div>
 <table>
 <thead>
 <tr>
 <th>#</th>
 <th>이름</th>
 </tr>
 </thead>
 <tbody>
 <tr th:each="member : ${members}">
 <td th:text="${member.id}"></td>
 <td th:text="${member.name}"></td>
 </tr>
 </tbody>
 </table>
 </div>
</div> <!-- /container -->
</body>
</html>
```



