package three.finalproject.domain.data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import three.finalproject.domain.product.Product;
import three.finalproject.domain.product.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductDataInit {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {

        Product product = new Product();
        product.setProduct_no(1L);

    }
}
