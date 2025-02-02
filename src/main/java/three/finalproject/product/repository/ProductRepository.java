package three.finalproject.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.product.domain.dto.ProductDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class ProductRowMapper implements RowMapper<ProductDTO> {
        @Override
        public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductDTO product = new ProductDTO();
            product.setProduct_no(rs.getLong("product_no"));
            product.setCategory_no(rs.getLong("category_no"));
            product.setName(rs.getString("name"));
            product.setCompany(rs.getString("company"));
            product.setIn_price(rs.getInt("in_price"));
            product.setOut_price(rs.getInt("out_price"));
            product.setQuantity(rs.getInt("quantity"));
            product.setSeal_service(rs.getString("seal_service").equals("True"));
            return product;
        }
    }

    public void save(ProductDTO product) {
        String sql = "INSERT INTO product_table (category_no, name, company, in_price, out_price, quantity, seal_service) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getCategory_no(), product.getName(), product.getCompany(), product.getIn_price(),
                product.getOut_price(), product.getQuantity(), product.getSeal_service() ? "True" : "False");
    }

    public List<ProductDTO> findAll() {
        String sql = "SELECT * FROM product_table";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public Optional<ProductDTO> findById(Long product_no) {
        String sql = "SELECT * FROM product_table WHERE product_no = ?";
        List<ProductDTO> products = jdbcTemplate.query(sql, new ProductRowMapper(), product_no);
        return products.stream().findFirst();
    }

    public void update(Long product_no, ProductDTO updatedProduct) {
        String sql = "UPDATE product_table SET category_no = ?, name = ?, company = ?, in_price = ?, out_price = ?, quantity = ?, seal_service = ? WHERE product_no = ?";
        jdbcTemplate.update(sql, updatedProduct.getCategory_no(), updatedProduct.getName(), updatedProduct.getCompany(),
                updatedProduct.getIn_price(), updatedProduct.getOut_price(), updatedProduct.getQuantity(),
                updatedProduct.getSeal_service() ? "True" : "False", product_no);
    }

    public void delete(Long product_no) {
        String sql = "DELETE FROM product_table WHERE product_no = ?";
        jdbcTemplate.update(sql, product_no);
    }
}
