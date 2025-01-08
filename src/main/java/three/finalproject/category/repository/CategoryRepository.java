package three.finalproject.category.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.category.domain.Category;
import three.finalproject.category.domain.dto.CategoryDTO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;


    private final RowMapper<CategoryDTO> categoryRowMapper = (rs, rowNum) -> {
        CategoryDTO category = new CategoryDTO();
        category.setCategory_no(rs.getLong("category_no"));
        category.setName(rs.getString("name"));
        category.setDelete(rs.getString("delete"));
        return category;
    };

    public CategoryDTO save(CategoryDTO category) {
        String sql = "INSERT INTO category_table (name, delete) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getDelete());
        Long id = jdbcTemplate.queryForObject("SELECT IDENTITY()", Long.class);
        category.setCategory_no(id);
        return category;
    }

    public List<CategoryDTO> findAll() {
        String sql = "SELECT * FROM category_table";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public void update(Long category_no, CategoryDTO updateParam) {
        String sql = "UPDATE category_table SET name = ?, delete = ? WHERE category_no = ?";
        jdbcTemplate.update(sql, updateParam.getName(), updateParam.getDelete(), category_no);
    }

}
