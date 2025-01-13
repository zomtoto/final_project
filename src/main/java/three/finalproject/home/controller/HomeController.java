package three.finalproject.home.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import three.finalproject.home.domain.dto.HomeDTO;
import three.finalproject.home.repository.HomeRepository;
import three.finalproject.member.domain.dto.MemberDTO;

@Slf4j
@Controller
public class HomeController {

    private final HomeRepository homeRepository;

    public HomeController(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @GetMapping("/")
    public String homeLogin(HttpServletRequest request, Model model) {
        // 기존 세션이 없으면 홈 화면으로 이동
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "index/home";
        }

        // 세션에서 로그인 정보를 가져옴
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        // 로그인 정보가 없으면 홈 화면으로 이동
        if (loginMember == null) {
            return "index/home";
        }

        // 세션이 유지되면 로그인 홈으로 이동
        int memberCount = homeRepository.getMemberCount(); //현재 누적 회원 개수
        Long buyAmount = homeRepository.getBuyAmount(); //현재 판매 누적 금액

        model.addAttribute("memberCount", memberCount);
        model.addAttribute("buyAmount", buyAmount);
        model.addAttribute("member", loginMember);
        return "index/loginHome";
    }

}
