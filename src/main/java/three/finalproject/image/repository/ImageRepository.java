package three.finalproject.image.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.image.domain.dto.ImageDTO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper to map result set rows to Image objects
    private final RowMapper<ImageDTO> imageRowMapper = (rs, rowNum) -> {
        ImageDTO image = new ImageDTO();
        image.setImage_no(rs.getLong("image_no"));
        image.getProduct().setProduct_no(rs.getLong("product_no"));
        image.setOrigin_path(rs.getString("origin_path"));
        image.setSave_path(rs.getString("save_path"));
        image.setSave_date(rs.getString("save_date"));
        image.setUpdate_date(rs.getString("update_date"));
        image.setDelete(rs.getString("delete"));
        return image;
    };

    public void save(ImageDTO image) {
        String sql = "INSERT INTO image_table (product_no, origin_path, save_path, save_date, update_date, delete) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, image.getProduct().getProduct_no(), image.getOrigin_path(), image.getSave_path(),
                image.getSave_date(), image.getUpdate_date(), image.getDelete());
    }


    public List<ImageDTO> findByProductNo(Long product_no) {
        String sql = "SELECT * FROM image_table WHERE product_no = ? AND delete = 'F'";
        return jdbcTemplate.query(sql, imageRowMapper, product_no);
    }

    public List<ImageDTO> getAllImages() {
        String sql ="SELECT * FROM image_table";
        return jdbcTemplate.query(sql, imageRowMapper);
    }

    public ImageDTO findByNo(Long image_no) {
        String sql = "SELECT * FROM image_table WHERE image_no = ?";
        return jdbcTemplate.queryForObject(sql, imageRowMapper, image_no);
    }


    public void update(Long image_no, ImageDTO updateImage) {
        String sql = "UPDATE image_table SET origin_path = ?, save_path = ?, update_date = ?, delete = ? " +
                "WHERE image_no = ?";
        jdbcTemplate.update(sql, updateImage.getOrigin_path(), updateImage.getSave_path(),
                updateImage.getUpdate_date(), updateImage.getDelete(), image_no);
    }

    public void softDelete(Long image_no) {
        String sql = "UPDATE image_table SET delete = 'T' WHERE image_no = ?";
        jdbcTemplate.update(sql, image_no);
    }

    public void deleteByProductNo(Long product_no) {
        String sql = "UPDATE image_table SET delete = 'T' WHERE product_no = ?";
        jdbcTemplate.update(sql, product_no);
    }
}
