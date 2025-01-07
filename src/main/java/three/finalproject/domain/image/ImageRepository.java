package three.finalproject.domain.image;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    public ImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to map result set rows to Image objects
    private final RowMapper<Image> imageRowMapper = (rs, rowNum) -> {
        Image image = new Image();
        image.setImage_no(rs.getLong("image_no"));
        image.setProduct_no(rs.getLong("product_no"));
        image.setOrigin_path(rs.getString("origin_path"));
        image.setSave_path(rs.getString("save_path"));
        image.setSave_date(rs.getString("save_date"));
        image.setUpdate_date(rs.getString("update_date"));
        image.setDelete(rs.getString("delete"));
        return image;
    };

    /**
     * Save a new image record in the database.
     *
     * @param image The image object to save.
     */
    public void save(Image image) {
        String sql = "INSERT INTO image_table (product_no, origin_path, save_path, save_date, update_date, delete) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, image.getProduct_no(), image.getOrigin_path(), image.getSave_path(),
                image.getSave_date(), image.getUpdate_date(), image.getDelete());
    }

    /**
     * Retrieve all images associated with a product.
     *
     * @param product_no The product_no to retrieve images for.
     * @return List of images.
     */
    public List<Image> findByProductNo(Long product_no) {
        String sql = "SELECT * FROM image_table WHERE product_no = ? AND delete = 'F'";
        return jdbcTemplate.query(sql, imageRowMapper, product_no);
    }

    /**
     * Retrieve a specific image by its image_no.
     *
     * @param image_no The image_no to retrieve.
     * @return The image object.
     */
    public Image findByNo(Long image_no) {
        String sql = "SELECT * FROM image_table WHERE image_no = ?";
        return jdbcTemplate.queryForObject(sql, imageRowMapper, image_no);
    }

    /**
     * Update an existing image record in the database.
     *
     * @param image_no The ID of the image to update.
     * @param updateImage The updated image object.
     */
    public void update(Long image_no, Image updateImage) {
        String sql = "UPDATE image_table SET origin_path = ?, save_path = ?, update_date = ?, delete = ? " +
                "WHERE image_no = ?";
        jdbcTemplate.update(sql, updateImage.getOrigin_path(), updateImage.getSave_path(),
                updateImage.getUpdate_date(), updateImage.getDelete(), image_no);
    }

    /**
     * Soft delete an image by setting its delete column to 'T'.
     *
     * @param image_no The ID of the image to delete.
     */
    public void softDelete(Long image_no) {
        String sql = "UPDATE image_table SET delete = 'T' WHERE image_no = ?";
        jdbcTemplate.update(sql, image_no);
    }

    /**
     * Delete all images associated with a product by setting delete to 'T'.
     *
     * @param product_no The product_no to soft delete images for.
     */
    public void deleteByProductNo(Long product_no) {
        String sql = "UPDATE image_table SET delete = 'T' WHERE product_no = ?";
        jdbcTemplate.update(sql, product_no);
    }
}
