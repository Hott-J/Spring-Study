### :smile: Form
- **Form**
  - *name* : form의 이름, 서버로 보내질 때 이름의 값으로 데이터 전송
  - *action* : form이 전송되는 서버 url 또는 html 링크
  - *method* : 전송 방법 설정. get은 default, post는 데이터를 url에 공개하지 않고 숨겨서 전송하는 방법

- [참고](https://velog.io/@choiiis/HTMLCSS-form-%ED%83%9C%EA%B7%B8-%EC%A0%95%EB%A6%AC)

### :smile: 회원 등록 동작 원리

**1. MemberController**
```java
@GetMapping("/members/new") //데이터 조회할때 보통 get 사용
   public String createForm(){
       return "members/createMemberForm";
   }
```

- url 에 `localhost/members/new` 를 입력하면 이는 GET 방식이다. 조회를 할 때 주로 사용된다.
- ViewResolver 가 작동해, template 폴더안에 members/createMemberForm.html 을 찾아 실행한다.

**2. createMemberForm.html**
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <form action="/members/new" method="post">
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름을
입력하세요">
        </div>
        <button type="submit">등록</button>
    </form>
</div> <!-- /container -->
</body>
</html>
```

- form 태그안에 **action** 태그와 **method** 태그를 통해 `/members/new` url로 `POST` 방식으로 form 태그 안의 데이터가 전송된다.

**3. MemberController/@PostMaaping("/members/new")**
```java
 @PostMapping("members/new") //html 에서 버튼 누르면 post로 members/new로 넘어옴. 데이터를 등록할때 보통 post 사용
    public String create(@ModelAttribute MemberForm form){
        Member member=new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/"; //홈 화면으로 돌려버림
    }
 ```
 
 - MemberForm 객체를 파라미터로 넣는다.
 
**4. MemberForm**
```java
public class MemberForm {
    private String name; //html에서 입력된 name이 들어온다.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

- 스프링 MVC가 HTTP 파라미터로 넘어오는 모든 이름들을 인식해서 (name) MemberForm 객체 (직접 만든 아무 객체든 상관없음)에 동일한 프로퍼티 이름이 있으면 찾아서 넣어준다.
- 자바빈 프토퍼티 규약에 의해 getName , setName 으로 메소드 선언
  - *private String name1 로 선언할 경우, getName1 / setName1 과 같이 자바빈 프로퍼티 규약을 지켜야한다.*
- 3번으로 올라가 name이 파라미터로 전송되고 `form.getName()` 통해 불러와진 다음 `member.setName`에 의해 member의 name에 저장된다.
