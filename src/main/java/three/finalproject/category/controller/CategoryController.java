package three.finalproject.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import three.finalproject.category.repository.CategoryRepository;
import three.finalproject.category.domain.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/category/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // List all categories
    @GetMapping
    public String categories(Model model) {
        List<CategoryDTO> categoryList = categoryRepository.findAll();
        model.addAttribute("category", categoryList);
        return "category/categories";
    }

    // Show category details
    @GetMapping("/{category_no}")
    public String categoryDetail(@PathVariable long category_no, Model model) {
        Optional<CategoryDTO> category = categoryRepository.findById(category_no);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "category/category";
        }
        return "redirect:/category/categories";  // Redirect if category not found
    }

    // Show add category form
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "category/categoryAddForm";
    }

    // Handle adding a new category
    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") CategoryDTO category, BindingResult bindingResult) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("category", "name", "카테고리 이름은 필수입니다."));
        }
        if (bindingResult.hasErrors()) {
            return "category/categoryAddForm";
        }
        categoryRepository.save(category);
        return "redirect:/category/categories";
    }

    // Show edit category form
    @GetMapping("/{category_no}/edit")
    public String editForm(@PathVariable long category_no, Model model) {
        Optional<CategoryDTO> category = categoryRepository.findById(category_no);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "category/categoryEditForm";
        }
        return "redirect:/category/categories"; // Redirect if not found
    }

    // Handle editing a category
    @PostMapping("/{category_no}/edit")
    public String editCategory(@PathVariable long category_no, @ModelAttribute("category") CategoryDTO category,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("category", "name", "카테고리 이름은 필수입니다."));
        }
        if (bindingResult.hasErrors()) {
            return "category/categoryEditForm";
        }

        categoryRepository.update(category_no, category);
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/category/categories";
    }

    // Handle deleting a category
    @PostMapping("/{category_no}/delete")
    public String deleteCategory(@PathVariable long category_no, RedirectAttributes redirectAttributes) {
        Optional<CategoryDTO> category = categoryRepository.findById(category_no);
        if (category.isPresent()) {
            categoryRepository.delete(category_no);
            redirectAttributes.addFlashAttribute("status", "deleted");
        }
        return "redirect:/category/categories";
    }
}
