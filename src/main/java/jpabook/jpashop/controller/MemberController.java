package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        //controller에서 뷰로 넘어갈 때 데이터(memberFrom)를 실어서 넘김

        return "members/createMemberForm";
    }//controller가 화면을 이동할 때 MemberForm 이라는 빈 껍데기 객체를 가져감
    //validation 같은 것을 해주기 위해서 객체를 가져간다.


    @PostMapping("/members/new") //Get은 홈 화면을 여는 것이고 Post는 실제 데이터를 등록하는 것
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
            //타임리프는 integration이 잘 되어 있다
            //스프링이 BindingResult를 화면까지 끌고 가준다. 그래서 어떤 에러가 있는지를
            //members/createMemberForm 화면에서 알려준다.
        }//그러면 MemberForm에서 @NotEmpty(message = "회원 이름은 필수 입니다.") 를
        //createMemberForm 화면에서 알려주는 것이다.

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); //호출하면 저장이 됨.

        return "redirect:/"; //저장이 되고 난 다음에 재로딩되면 좋지 않기 때문에 리다이렉트.
    }//그러면 첫 번째 페이지로 넘어감
    //즉 회원가입 입력하고 Submit 하면 첫 번째 페이지로 넘어가는 것


    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        //"members" 가 키고 키를 꺼내면 members 리스트가 나옴
        //변수를 압축시킬 수 있음 -> Ctrl+Alt+N
        return "members/memberList";
    }//Member Entity를 그대로 뿌리기 보다는 실무적으로 복잡해지면
    // DTO로 변환해서 화면에 꼭 필요한 데이터만 가지고 출력하는 것이 권장.
}//지금 이것은 화면에 뿌리는데 Entity를 손댈 게 없는 상황이어서 Member를 그대로 반환함.

//템플릿 엔진에서 랜더링할 때는 어차피 서버 안에서 기능을 돌기 때문에 Member를 화면 템플릿에
//전달해도 괜찮다. -> 서버 내에서 내가 원하는 데이터만 찍어서 출력하기 때문에

//API 할 때는 이유불문하고 절대 외부에 Entity를 넘기면 안됌.
//API는 스펙인데 Entity를 반환하게 되면 Member에 password를 노출시키고 API 스펙이 변해버린다.
//Entity 로직을 추가했는데 이것 때문에 API 스펙이 변해버린다. (불안정한 API가 됌)

//@Valid :
//BindingResult: form에서 오류가 있으면 코드가 넘어가지 않고 튕김.
//근데 @Valid 하고 난 다음에 BindingResult가 있으면 result에 오류가 담겨서 실행이 된다.