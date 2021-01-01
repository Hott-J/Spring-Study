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
  * MemberController에 GetMapping("/members/new") 추가
  * templates폴더 밑에 members폴더 생성 후 createMembersForm.html 파일 생성
  ```java
  @Controller
  public class MemberController {

      private final MemberService memberService; // 하나만 만들어져 있으면 된다.

      @Autowired
      public MemberController(MemberService memberService) {
          this.memberService = memberService;
      }

      @GetMapping("/members/new")
      public String createForm() {
          return "members/createMembersForm";
      }
  }
  ```
  
* 웹 등록 화면에서 데이터를 전달 받을 폼 객체
    * templates/members/createMembersForm.html 에 있는 name="name"과 매칭된다.
    ```java
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
    
* 회원 Controller에서 회원을 실제 등록하는 기능
    * MemberController에 PostMapping("/members/new") 추가
    * PostMapping : form에 data를 넣어서 전달, 등록 / GetMapping : data 조회
    ```java
    @Controller
    public class MemberController {

        private final MemberService memberService; // 하나만 만들어져 있으면 된다.

        @Autowired
        public MemberController(MemberService memberService) {
            this.memberService = memberService;
        }

        @GetMapping("/members/new")
        public String createForm() {
            return "members/createMembersForm";
        }

        @PostMapping("/members/new")
        public String create(MemberForm form) {
            Member member = new Member();
            member.setName(form.getName());
            memberService.join(member);
            return "redirect:/";
        }
    }
    ```

### 3. 회원 웹 기능 - 조회
* 회원 Controller에서 조회 기능
  * GetMapping("members") 추가
  ```java
  @Controller
    public class MemberController {

        private final MemberService memberService; // 하나만 만들어져 있으면 된다.

        @Autowired
        public MemberController(MemberService memberService) {
            this.memberService = memberService;
        }

        @GetMapping("/members/new")
        public String createForm() {
            return "members/createMembersForm";
        }

        @PostMapping("/members/new")
        public String create(MemberForm form) {
            Member member = new Member();
            member.setName(form.getName());

            memberService.join(member);

            return "redirect:/";
        }

        @GetMapping("/members")
        public String list(Model model) {
            List<Member> members = memberService.findMembers(); // 모든 회원 list가 들어감.
            model.addAttribute("members", members);
            return "members/memberList";
        }
    }
    ```
