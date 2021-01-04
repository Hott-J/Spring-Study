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
#### 🔶 회원 웹 기능 - 등록
```java
@PostMapping("/members/new")
public String create(MemberForm form){
    Member member = new Member();
    member.setName(form.getName());

    memberService.join(member);

    return "redirect:/";
}
```
⚡**동작과정**
* local:8080/members/new url을 입력한다.
* GetMapping("members/new") 어노테이션을 보고 members/createMemberForm.html을 브라우저에 뿌린다.
* 사용자가 form 태그에 등록할 이름을 적고 등록 버튼을 누른다.
* < input > 태그의 name에 등록할 이름이 넘어가고 MemberForm의 name에 setName method가 호출되면서 name이 저장된다.
* < form action="/members/new" method="post" > 태그를 보고 이에 해당하는 MemberController의 postMapping 어노테이션을 확인한다.
* 새로 등록할 member 객체를 생성하고 getName()로 member의 이름을 꺼내서 새로운 객체의 이름을 setName()로 지정한다.
* MemberService의 join()으로 db에 저장한다.

📝 return "redirect:/" 로 인해서 등록 버튼을 누르면 localhost:8080/ 로 이동한다.

#### 🔶 회원 웹 기능 - 조회
```java
@GetMapping("/members")
public String list(Model model){
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
}
```

📝Model interface 주요 메서드<br>
org.springframework.ui.Model 인터페이스는 모델을 설정할 수 있도록 다음과 같은 메서드를 제공하고 있다.

* *addAttribute(String name, Object value)*<br>
value 객체를 name 이름으로 추가한다. 뷰 코드에서는 name으로 지정한 이름을 통해서 value를 사용한다.
* *addAllAttributes(Map<String, ?> attributes)*<br>
Map에 포함된 <키, 값>에 대해 키를 모델 이름으로 사용해서 값을 모델로 추가한다.
* *boolean containsAttributes(String name)*<br>
지정한 이름의 모델 객체를 포함하고 있는 경우 true를 반환하다.

⚡**동작과정**
* localhost:8080/members/ url을 입력한다.
* @GetMapping("/members") 어노테이션을 보고 memberService의 findMembers() 를 실행해 members list를 "members"라는 이름의 모델로 설정한다.
* memberList.html에서 thymeleaf 템플릿이 members 객체를 member에 매칭한다.
* th:each 태그는 member list를 순회한다.
* member.id와 member.name은 각각 getId(), getName() 메소드를 실행하여 값을 반환한다.
* member list를 다 순회하면 랜더링된 memberList.html을 사용자에게 반환한다.