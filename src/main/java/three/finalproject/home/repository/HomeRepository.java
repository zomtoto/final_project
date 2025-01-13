package three.finalproject.home.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.home.domain.dto.HomeDTO;
import three.finalproject.member.domain.dto.MemberDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HomeRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class HomeRowMapper implements RowMapper<HomeDTO> {
        @Override
        public HomeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            HomeDTO home = new HomeDTO();
            home.getYear().setYear_year(rs.getInt("year_year"));
            home.getMember().setName(rs.getString("member_name")); //rename name-> member_name
            home.getProduct().setName(rs.getString("product_name")); //rename name-> product_name
            home.setProduct_quantity(rs.getInt("product_quantity")); //상품 누정 개수
            home.setYearly_amount(rs.getInt("yearly_amount")); //연도별 누적 판매 금액

            home.getProduct().setProduct_no(rs.getLong(("product_no")));
            home.getMember().setMember_no(rs.getLong(("member_no")));
            return home;
        }
    }

    public int getMemberCount() {
        String sql = "select count(*) from member_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Long getBuyAmount() {
        String sql = "select sum(TOTAL_PRICE) from buy_table";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long getYearlyAmount(int year_year) {
        String sql = """
                select sum(TOTAL_PRICE) from BUY_TABLE
                where date 
                between ? and ?
                """;
        return jdbcTemplate.queryForObject(sql, Long.class, year_year, year_year-1);
    }

}
