package three.finalproject.analyze.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.analyze.domain.dto.AnalyzeDTO;
import three.finalproject.analyze.domain.dto.YearDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class YearRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper for YearDTO
    private static class YearRowMapper implements RowMapper<YearDTO> {
        @Override
        public YearDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            YearDTO year = new YearDTO();
            //year.setYear_no(rs.getLong("year_no"));
            year.setAnalyze_year(rs.getInt("analyze_year"));
            return year;
        }
    }

    //Find all year_no in year_table
    public List<YearDTO> findAllYear() {
        String sql = """
                SELECT year_year AS analyze_year
                FROM year_table
                """;
        return jdbcTemplate.query(sql,new YearRowMapper());
    }
}
