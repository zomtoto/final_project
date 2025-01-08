package three.finalproject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import three.finalproject.member.domain.Member;
import three.finalproject.session.SessionConst;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String homeLogin(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if(session == null) {
            return "index/home";
        }

        Member loginUser = (Member)session.getAttribute(SessionConst.LOGIN_USER);

        //세션에 회원 데이터가 없으면 home
        if(loginUser == null) {
            return "index/home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginUser);
        return "index/loginHome";
    }
}
