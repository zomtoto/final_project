package three.finalproject.domain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import three.finalproject.domain.image.Image;
import three.finalproject.domain.image.ImageRepository;
import three.finalproject.domain.product.Product;
import three.finalproject.domain.product.ProductRepository;
import three.finalproject.domain.category.Category;
import three.finalproject.domain.category.CategoryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Display all products
     */
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/products"; // View for listing products
    }

    /**
     * Display a single product
     */
    @GetMapping("/{product_no}")
    public String product(@PathVariable long product_no, Model model) {
        Product product = productRepository.findByNo(product_no);
        model.addAttribute("product", product);
        List<Image> images = imageRepository.findByProductNo(product_no);
        model.addAttribute("images", images);
        return "product/product"; // View for a single product
    }

    /**
     * Display the add product form
     */
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "product/addProduct"; // View for adding a product
    }

    /**
     * Handle form submission for adding a product
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        log.info("Saving product: {}", product);

        // Validate product fields
        if (bindingResult.hasErrors()) {
            return "product/addProduct";
        }

        // Save product details to the database
        Product savedProduct = productRepository.save(product);

        // Handle the image file upload
        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = "src/main/resources/uploads";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                imageFile.transferTo(filePath.toFile());

                // Save the image metadata to the database
                imageRepository.save(new Image(
                        null,
                        savedProduct.getProduct_no(),
                        fileName,
                        "/uploads/" + fileName,
                        LocalDate.now().toString(),
                        null,
                        "F"
                ));

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("imageFile", "uploadError", "이미지 업로드에 실패했습니다.");
                return "product/addProduct";
            }
        }

        redirectAttributes.addAttribute("product_no", savedProduct.getProduct_no());
        return "redirect:/product/products";
    }

    /**
     * Display the edit product form
     */
    @GetMapping("/{product_no}/edit")
    public String editProductForm(@PathVariable long product_no, Model model) {
        Product product = productRepository.findByNo(product_no);
        model.addAttribute("product", product);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "product/editProduct"; // View for editing a product
    }

    /**
     * Handle form submission for editing a product
     */
    @PostMapping("/{product_no}/edit")
    public String editProduct(@PathVariable long product_no,
                              @ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {

        // Update the product in the database
        productRepository.update(product_no, product);

        // Handle image update if a new file is uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = "src/main/resources/uploads";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                imageFile.transferTo(filePath.toFile());

                // Save updated image metadata
                imageRepository.save(new Image(
                        null,
                        product_no,
                        fileName,
                        "/uploads/" + fileName,
                        LocalDate.now().toString(),
                        null,
                        "F"
                ));

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "이미지 업로드에 실패했습니다.");
            }
        }

        return "redirect:/product/products/{product_no}";
    }

    /**
     * Delete a product
     */
    @PostMapping("/{product_no}/delete")
    public String deleteProduct(@PathVariable long product_no, RedirectAttributes redirectAttributes) {
        productRepository.delete(product_no);
        imageRepository.deleteByProductNo(product_no);
        redirectAttributes.addFlashAttribute("success", "상품이 삭제되었습니다.");
        return "redirect:/product/products";
    }
}
