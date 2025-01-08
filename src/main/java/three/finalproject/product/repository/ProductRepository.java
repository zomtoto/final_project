package three.finalproject.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.product.domain.dto.ProductDTO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProductDTO> productRowMapper = (rs, rowNum) -> {
        ProductDTO product = new ProductDTO();
        product.setCategory_no(rs.getLong("category_no"));
        product.setName(rs.getString("name"));
        product.setCompany(rs.getString("company"));
        product.setIn_price(rs.getInt("in_price"));
        product.setOut_price(rs.getInt("out_price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setSeal_service("True".equals(rs.getString("seal_service")));
        return product;
    };

    public ProductDTO save(ProductDTO product) {
        String sql = "INSERT INTO product_table (category_no, name, company, in_price, out_price, quantity, sell_count, seal_service) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    product.getCategory_no(),
                    product.getName(),
                    product.getCompany(),
                    product.getIn_price(),
                    product.getOut_price(),
                    product.getQuantity(),
                    0, // Default sell_count
                    product.getSeal_service() ? "True" : "False"
            );

            Long product_no = jdbcTemplate.queryForObject("SELECT IDENTITY()", Long.class);
            if (product_no != null) {
                product.setProduct_no(product_no);
            }
            return product;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save product: " + product, e);
        }
    }

    public ProductDTO findByNo(Long product_no) {
        String sql = "SELECT * FROM product_table WHERE product_no = ?";
        try {
            return jdbcTemplate.queryForObject(sql, productRowMapper, product_no);
        } catch (Exception e) {
            return null; // Return null if the product is not found
        }
    }

    public List<ProductDTO> findAll() {
        String sql = "SELECT * FROM product_table WHERE delete = 'F'";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public void update(Long product_no, ProductDTO updatedProduct) {
        String sql = "UPDATE product_table SET category_no = ?, name = ?, company = ?, in_price = ?, out_price = ?, " +
                "quantity = ?, seal_service = ?, delete = ? WHERE product_no = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    updatedProduct.getCategory_no(),
                    updatedProduct.getName(),
                    updatedProduct.getCompany(),
                    updatedProduct.getIn_price(),
                    updatedProduct.getOut_price(),
                    updatedProduct.getQuantity(),
                    updatedProduct.getSeal_service() != null ? (updatedProduct.getSeal_service() ? "True" : "False") : "False",
                    updatedProduct.getDelete() != null ? (updatedProduct.getDelete() ? "True" : "False") : "False",
                    product_no);

            if (rowsAffected == 0) {
                throw new RuntimeException("Update failed: Product with product_no " + product_no + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + updatedProduct, e);
        }
    }


    public void delete(Long product_no) {
        String sql = "UPDATE product_table SET delete = 'T' WHERE product_no = ?";
        jdbcTemplate.update(sql, product_no);
    }
}
