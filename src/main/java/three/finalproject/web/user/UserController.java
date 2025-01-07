package three.finalproject.web.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import three.finalproject.domain.user.User;
import three.finalproject.domain.user.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("user") User user) {

        return "users/addUserForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "users/addUserForm";
        }

        userRepository.save(user);
        return "redirect:/users";
    }

}
