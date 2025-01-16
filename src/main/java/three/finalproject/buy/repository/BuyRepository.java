package three.finalproject.buy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.buy.domain.dto.BuyDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuyRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class buyRowMapper implements RowMapper<BuyDTO> {
        @Override
        public BuyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            BuyDTO buy = new BuyDTO();
            buy.setBuy_no(rs.getLong("buy_no"));
            buy.getMember().setMember_no(rs.getLong("member_no"));
            buy.getProduct().setProduct_no(rs.getLong("product_no"));
            buy.setDate(rs.getString("date"));
            buy.setQuantity(rs.getInt("quantity"));
            buy.setSeal_service(rs.getString("seal_service"));
            buy.setTotal_price(rs.getInt("total_price"));
            buy.setMethod(rs.getString("method"));

            buy.getMember().setName(rs.getString("member_name"));
            buy.getProduct().setName(rs.getString("product_name"));
            return buy;
        }
    }

    public BuyDTO save(BuyDTO buy) {
        String sql = "INSERT INTO buy_table (member_no, product_no, date, quantity, seal_service, total_price, method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, buy.getMember().getMember_no(), buy.getProduct().getProduct_no(), buy.getDate(),
                buy.getQuantity(), buy.getSeal_service(), buy.getTotal_price(), buy.getMethod());
        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);
        buy.setBuy_no(id);
        return buy;
    }

    public BuyDTO findByNo(Long buy_no) {
        String sql = "SELECT * FROM buy_table WHERE buy_no = ?";
        return jdbcTemplate.queryForObject(sql, new buyRowMapper(), buy_no);
    }

    public List<BuyDTO> findAll() {
        String sql = """
                    SELECT b.*, p.name as product_name, m.name as member_name
                    FROM buy_table b
                    JOIN product_table p on b.product_no = p.product_no
                    JOIN member_table m on b.member_no = m.member_no
                    order by b.buy_no
                    """;
        return jdbcTemplate.query(sql, new buyRowMapper());
    }

    public List<BuyDTO> findPaginated(int offset, int limit) {
        String sql = """
                    SELECT b.*, p.name AS product_name, m.name AS member_name
                            FROM buy_table b
                            JOIN product_table p ON b.product_no = p.product_no
                            JOIN member_table m ON b.member_no = m.member_no
                            ORDER BY b.buy_no
                            LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, new Object[]{limit, offset}, (rs, rowNum) -> {
            BuyDTO buy = new BuyDTO();
            buy.setBuy_no(rs.getLong("buy_no"));
            buy.getMember().setMember_no(rs.getLong("member_no"));
            buy.getProduct().setProduct_no(rs.getLong("product_no"));
            buy.setDate(rs.getString("date"));
            buy.setQuantity(rs.getInt("quantity"));
            buy.setSeal_service(rs.getString("seal_service"));
            buy.setTotal_price(rs.getInt("total_price"));
            buy.setMethod(rs.getString("method"));

            buy.getMember().setName(rs.getString("member_name"));
            buy.getProduct().setName(rs.getString("product_name"));
            return buy;
                });
        //return jdbcTemplate.query(sql, new buyRowMapper(), offset, limit);
    }

    public int countBuys() {
        String sql = "SELECT COUNT(*) from buy_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
