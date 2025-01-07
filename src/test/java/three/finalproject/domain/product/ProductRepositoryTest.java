/*
package three.finalproject.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProductRepositoryTest {

    ProductRepository productRepository = new ProductRepository();

    @AfterEach
    void afterEach() {
        productRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Product product = new Product("ProductA", 10000, 10);

        //when
        Product savedProduct = productRepository.save(product);

        //then
        Product findProduct = productRepository.findById(savedProduct.getId());
        assertThat(findProduct).isEqualTo(savedProduct);
    }

    @Test
    void findAll() {
        //given
        Product product2 = new Product("ProductB", 20000, 20);
        Product product3 = new Product("ProductC", 30000, 30);

        productRepository.save(product2);
        productRepository.save(product3);

        //when
        List<Product> result = productRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(product2, product3);

    }

    @Test
    void updateProduct() {
        //given
        Product product1 = new Product("Product1", 15000, 15);

        Product savedProduct = productRepository.save(product1);
        Long productId = savedProduct.getId();

        //when
        Product updateParam = new Product("Product2", 16000, 16);
        productRepository.update(productId, updateParam);

        //then
        Product findProduct = productRepository.findById(productId);

        assertThat(findProduct.getProductName()).isEqualTo(updateParam.getProductName());
        assertThat(findProduct.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findProduct.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

}
 */