package three.finalproject.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import three.finalproject.category.domain.dto.CategoryDTO;
import three.finalproject.image.repository.ImageRepository;
import three.finalproject.category.repository.CategoryRepository;
import three.finalproject.image.domain.dto.ImageDTO;
import three.finalproject.product.domain.dto.ProductDTO;
import three.finalproject.product.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/product/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public String products(Model model) {
        List<ProductDTO> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/{product_no}")
    public String productDetail(@PathVariable long product_no, Model model) {
        Optional<ProductDTO> product = productRepository.findById(product_no);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/product";
        }
        return "redirect:/product/products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        List<CategoryDTO> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "product/productAddForm";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") ProductDTO product, BindingResult bindingResult) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("product", "name", "상품명은 필수입니다."));
        }
        if (bindingResult.hasErrors()) {
            return "product/productAddForm";
        }
        productRepository.save(product);
        return "redirect:/product/products";
    }

    @GetMapping("/{product_no}/edit")
    public String editProductForm(@PathVariable long product_no, Model model) {
        Optional<ProductDTO> product = productRepository.findById(product_no);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            List<CategoryDTO> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "product/productEditForm";
        }
        return "redirect:/product/products";
    }

    @PostMapping("/{product_no}/edit")
    public String editProduct(@PathVariable long product_no, @ModelAttribute("product") ProductDTO product,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("product", "name", "상품명은 필수입니다."));
        }
        if (bindingResult.hasErrors()) {
            return "product/productEditForm";
        }
        productRepository.update(product_no, product);
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/product/products";
    }

    @PostMapping("/{product_no}/delete")
    public String deleteProduct(@PathVariable long product_no, RedirectAttributes redirectAttributes) {
        Optional<ProductDTO> product = productRepository.findById(product_no);
        if (product.isPresent()) {
            productRepository.delete(product_no);
            redirectAttributes.addFlashAttribute("status", "deleted");
        }
        return "redirect:/product/products";
    }
}
