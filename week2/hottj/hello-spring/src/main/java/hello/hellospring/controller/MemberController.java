package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired //멤버서비스의 컨테이너에 딱 연결. 스프링빈에 등록되어있는 멤버서비스 객체를 넣어준다. 디펜던시 인젝션. 의존관계 주입.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new") //데이터 조회할때 보통 get 사용
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("members/new") //html 에서 버튼 누르면 post로 members/new로 넘어옴. 데이터를 등록할때 보통 post 사용
    public String create(MemberForm form){
        Member member=new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/"; //홈 화면으로 돌려버림
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members); //members 리스트를 담는다. 그리고 memberList.html에서 루프를 돈다.
        return "members/memberList";
    }
}
