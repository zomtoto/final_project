package three.finalproject.web.basic;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import three.finalproject.domain.user.User;
import three.finalproject.domain.user.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/users")
@RequiredArgsConstructor
public class BasicUserController {

    private final UserRepository userRepository;

    @GetMapping
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "basic/users";
    }

    @GetMapping("/{id}")
    public String user(@PathVariable long id, Model model) {
        User user = userRepository.findById(id);
        model.addAttribute("user", user);
        return "basic/user";
    }

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
