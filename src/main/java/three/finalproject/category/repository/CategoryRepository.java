package three.finalproject.category.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.category.domain.dto.CategoryDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class CategoryRowMapper implements RowMapper<CategoryDTO> {
        @Override
        public CategoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CategoryDTO category = new CategoryDTO();
            category.setCategory_no(rs.getLong("category_no"));
            category.setName(rs.getString("name"));
            category.setDelete(rs.getString("delete"));
            return category;
        }
    }

    // Save new category
    public void save(CategoryDTO category) {
        String sql = "INSERT INTO category_table (name, delete) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getDelete());
    }

    // Find all categories
    public List<CategoryDTO> findAll() {
        String sql = "SELECT * FROM category_table";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    // Find category by ID
    public Optional<CategoryDTO> findById(Long category_no) {
        String sql = "SELECT * FROM category_table WHERE category_no = ?";
        List<CategoryDTO> categories = jdbcTemplate.query(sql, new CategoryRowMapper(), category_no);
        return categories.stream().findFirst();
    }

    // Update category
    public void update(Long category_no, CategoryDTO updateParam) {
        String sql = "UPDATE category_table SET name = ?, delete = ? WHERE category_no = ?";
        jdbcTemplate.update(sql, updateParam.getName(), updateParam.getDelete(), category_no);
    }

    // Delete category
    public void delete(Long category_no) {
        String sql = "DELETE FROM category_table WHERE category_no = ?";
        jdbcTemplate.update(sql, category_no);
    }
}
