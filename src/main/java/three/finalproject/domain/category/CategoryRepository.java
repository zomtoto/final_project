package three.finalproject.domain.category;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setCategory_no(rs.getLong("category_no"));
        category.setName(rs.getString("name"));
        category.setDelete(rs.getString("delete"));
        return category;
    };

    public Category save(Category category) {
        String sql = "INSERT INTO category_table (name, delete) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getDelete());
        Long id = jdbcTemplate.queryForObject("SELECT IDENTITY()", Long.class);
        category.setCategory_no(id);
        return category;
    }

    public List<Category> findAll() {
        String sql = "SELECT * FROM category_table";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public void update(Long category_no, Category updateParam) {
        String sql = "UPDATE category_table SET name = ?, delete = ? WHERE category_no = ?";
        jdbcTemplate.update(sql, updateParam.getName(), updateParam.getDelete(), category_no);
    }

}
