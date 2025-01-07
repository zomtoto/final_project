package three.finalproject.domain.buy;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BuyRepository {

    private final JdbcTemplate jdbcTemplate;

    public BuyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Buy> buyRowMapper = (rs, rowNum) -> {
        Buy buy = new Buy();
        buy.setBuy_no(rs.getLong("buy_no"));
        buy.setMember_no(rs.getLong("member_no"));
        buy.setProduct_no(rs.getLong("product_no"));
        buy.setDate(rs.getString("date"));
        buy.setQuantity(rs.getInt("quantity"));
        buy.setSeal_service(rs.getString("seal_service"));
        buy.setTotal_price(rs.getInt("total_price"));
        buy.setMethod(rs.getString("method"));
        return buy;
    };

    public Buy save(Buy buy) {
        String sql = "INSERT INTO buy_table (member_no, product_no, date, quantity, seal_service, total_price, method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, buy.getMember_no(), buy.getProduct_no(), buy.getDate(),
                buy.getQuantity(), buy.getSeal_service(), buy.getTotal_price(), buy.getMethod());
        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);
        buy.setBuy_no(id);
        return buy;
    }

    public Buy findByNo(Long buy_no) {
        String sql = "SELECT * FROM buy_table WHERE buy_no = ?";
        return jdbcTemplate.queryForObject(sql, buyRowMapper, buy_no);
    }

    public List<Buy> findAll() {
        String sql = "SELECT * FROM buy_table";
        return jdbcTemplate.query(sql, buyRowMapper);
    }

    public void update(Long buy_no, Buy updateParam) {
        String sql = "UPDATE buy_table SET member_no = ?, product_no = ?, date = ?, quantity = ?, " +
                "seal_service = ?, total_price = ?, method = ? WHERE buy_no = ?";
        jdbcTemplate.update(
                sql,
                updateParam.getMember_no(),
                updateParam.getProduct_no(),
                updateParam.getDate(),
                updateParam.getQuantity(),
                updateParam.getSeal_service(),
                updateParam.getTotal_price(),
                updateParam.getMethod(),
                buy_no);
    }

    public void clearStore() {
        String sql = "DELETE FROM buy_table";
        jdbcTemplate.update(sql);
    }
}
