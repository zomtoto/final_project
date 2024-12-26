package three.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicController {

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }
}
