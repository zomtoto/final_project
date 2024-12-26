package three.project.domain.product;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

@Repository
public class ProductRespository {

    private static final Map<String, Product> store = new ConcurrentHashMap<>(); //static
    private AtomicLong sequence = new AtomicLong(1);


    public Product save(Product product) {
       String c_Code = product.getCategory_code().substring(0,1);
       String p_code = c_Code + Long.toString(sequence.getAndIncrement());
       product.setProduct_code(p_code);

       store.put(product.getProduct_code(), product);
       return product;
    }

}
