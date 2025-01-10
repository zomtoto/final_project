package three.finalproject.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import three.finalproject.member.domain.dto.LoginMemberDTO;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {

        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        /*
        * DB로부터 로그인한 회원 정보를 loginMember에 담아준다.
        * 추후에 MemberDTO를 따로 만들어 줘야한다.
        * 여기서 만들어놓은 MemberDTO는 어떠한 용도인지 알수가없다. 명확히 하는게 좋음
        * 로그인하는 용도이면 LoginSessionDTO 라 만들어 사용하는게 현명하다.
        * */
        LoginMemberDTO loginMember = loginService.login(form.getMember_id(), form.getMember_password());

        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login/loginForm";
        }

        session.setAttribute("loginMember", loginMember);
        /* 추후에 로그인쪽에서 유효성검사를 진행하게 될 경우
        * 아래와 같이 로그인 정보를 model의 속성에 담아 view에 넘겨야할대가 있다. 그럴때를 대비해서 아래에 주석처리 해놓겠다.*/
        /*model.addAttribute("loginMember", loginForm);*/
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
