package three.finalproject.domain.product;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to map result set rows to Product objects
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setProduct_no(rs.getLong("product_no"));
        product.setCategory_no(rs.getLong("category_no"));
        product.setName(rs.getString("name"));
        product.setCompany(rs.getString("company"));
        product.setIn_price(rs.getInt("in_price"));
        product.setOut_price(rs.getInt("out_price"));
        product.setSell_count(rs.getInt("sell_count"));
        product.setQuantity(rs.getInt("quantity"));
        product.setVisit(rs.getInt("visit"));
        product.setSeal_service(rs.getBoolean("seal_service"));
        product.setDelete(rs.getBoolean("delete"));
        return product;
    };

    /**
     * Save a new product to the database.
     *
     * @param product The product to save.
     * @return The saved product with the generated product_no.
     */
    public Product save(Product product) {
        String sql = "INSERT INTO product_table (category_no, name, company, in_price, out_price, sell_count, quantity, visit, seal_service, delete) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getCategory_no(), product.getName(), product.getCompany(),
                product.getIn_price(), product.getOut_price(), 0, product.getQuantity(),
                0, product.getSeal_service(), product.getDelete());
        Long product_no = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);
        product.setProduct_no(product_no);
        return product;
    }

    /**
     * Find a product by its product_no.
     *
     * @param product_no The product_no of the product to find.
     * @return The found product.
     */
    public Product findByNo(Long product_no) {
        String sql = "SELECT * FROM product_table WHERE product_no = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, product_no);
    }

    /**
     * Retrieve all products from the database.
     *
     * @return A list of all products.
     */
    public List<Product> findAll() {
        String sql = "SELECT * FROM product_table WHERE delete = 'F'";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    /**
     * Update an existing product in the database.
     *
     * @param product_no The product_no of the product to update.
     * @param updatedProduct The product object containing updated details.
     */
    public void update(Long product_no, Product updatedProduct) {
        String sql = "UPDATE product_table SET category_no = ?, name = ?, company = ?, in_price = ?, out_price = ?, " +
                "quantity = ?, seal_service = ?, delete = ? WHERE product_no = ?";
        jdbcTemplate.update(sql, updatedProduct.getCategory_no(), updatedProduct.getName(),
                updatedProduct.getCompany(), updatedProduct.getIn_price(), updatedProduct.getOut_price(),
                updatedProduct.getQuantity(), updatedProduct.getSeal_service(), updatedProduct.getDelete(), product_no);
    }

    /**
     * Soft delete a product by setting its delete flag to 'T'.
     *
     * @param product_no The product_no of the product to delete.
     */
    public void delete(Long product_no) {
        String sql = "UPDATE product_table SET delete = 'T' WHERE product_no = ?";
        jdbcTemplate.update(sql, product_no);
    }
}
