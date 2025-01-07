package three.finalproject.domain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import three.finalproject.domain.category.Category;
import three.finalproject.domain.category.CategoryRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/category/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public String categories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category/categories"; // List view page (not included here)
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/addCategory";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        // Validation logic
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("category", "name", "카테고리 이름은 필수입니다."));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "category/addCategory";
        }

        // Save category
        Category savedCategory = categoryRepository.save(category);
        redirectAttributes.addAttribute("category_no", savedCategory.getCategory_no());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/category/categories";
    }

    @PostMapping("/{category_no}/edit")
    public String editCategory(@PathVariable long category_no, @ModelAttribute Category category,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Validation logic
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("category", "name", "카테고리 이름은 필수입니다."));
        }

        if (bindingResult.hasErrors()) {
            return "category/editCategory";
        }

        // Update the category
        categoryRepository.update(category_no, category);

        redirectAttributes.addAttribute("category_no", category_no);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/category/categories";
    }

}
