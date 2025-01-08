package three.finalproject.buy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.buy.domain.dto.BuyDTO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuyRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<BuyDTO> buyRowMapper = (rs, rowNum) -> {
        BuyDTO buy = new BuyDTO();
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

    public BuyDTO save(BuyDTO buy) {
        String sql = "INSERT INTO buy_table (member_no, product_no, date, quantity, seal_service, total_price, method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, buy.getMember_no(), buy.getProduct_no(), buy.getDate(),
                buy.getQuantity(), buy.getSeal_service(), buy.getTotal_price(), buy.getMethod());
        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);
        buy.setBuy_no(id);
        return buy;
    }

    public BuyDTO findByNo(Long buy_no) {
        String sql = "SELECT * FROM buy_table WHERE buy_no = ?";
        return jdbcTemplate.queryForObject(sql, buyRowMapper, buy_no);
    }

    public List<BuyDTO> findAll() {
        String sql = "SELECT * FROM buy_table";
        return jdbcTemplate.query(sql, buyRowMapper);
    }

    public void update(Long buy_no, BuyDTO updateParam) {
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
