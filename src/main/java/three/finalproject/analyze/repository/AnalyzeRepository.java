package three.finalproject.analyze.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.analyze.domain.dto.AnalyzeDTO;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import three.finalproject.analyze.domain.dto.YearDTO;
import three.finalproject.member.domain.dto.MemberDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyzeRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper for AnalyzeDTO
    private static class AnalyzeRowMapper implements RowMapper<AnalyzeDTO> {
        @Override
        public AnalyzeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            AnalyzeDTO analyze = new AnalyzeDTO();
            analyze.getImage().setImage_no(rs.getLong("image_no"));
            analyze.getMember().setMember_no(rs.getLong("member_no"));
            analyze.setAnalyze_year(rs.getInt("analyze_year"));
            analyze.setGraph_type(rs.getString("graph_type"));
            analyze.getImage().setImage_name(rs.getString("image_name"));
            analyze.getImage().setImage_description(rs.getString("image_description"));
            analyze.getImage().setOrigin_path(rs.getString("origin_path"));
            return analyze;
        }
    }

    public Long addImage(String originPath, String savePath, String imageName, String imageDescription) {
        // SQL statement
        String sql = "INSERT INTO image_table (origin_path, save_path, image_name, image_description, save_date, update_date, delete) " +
                "VALUES (?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE, 'False')";

        // Use KeyHolder to retrieve the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"image_no"});
            ps.setString(1, originPath);
            ps.setString(2, savePath);
            ps.setString(3, imageName);
            ps.setString(4, imageDescription);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if(key == null) {
            throw new IllegalStateException(("Failed to retrive generated key for image_table."));
        }

        // Return the generated key
        return key.longValue();
    }

    // Add member_no and analysis details to analyze_table
    public void addAnalyze(AnalyzeDTO analyzeDTO) {
        String sql = "INSERT INTO analyze_table (image_no, member_no, analyze_year, graph_type) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                analyzeDTO.getImage().getImage_no(),
                analyzeDTO.getMember().getMember_no(),
                analyzeDTO.getAnalyze_year(),
                analyzeDTO.getGraph_type());

    }



    public List<AnalyzeDTO> findAll() {
        String sql = """
                SELECT i. *, a.MEMBER_NO, a.ANALYZE_YEAR, a.GRAPH_TYPE
                From image_table i
                JOIN analyze_table a on i.image_no = a.image_no
                """;
        return jdbcTemplate.query(sql, new AnalyzeRowMapper());
    }

    // Retrieve a single record from analyze_table by image_no
    public AnalyzeDTO getAnalyzeByImageNo(Long imageNo) {
        String sql = """
                SELECT i. *, a.MEMBER_NO, a.ANALYZE_YEAR, a.GRAPH_TYPE
                From image_table i
                JOIN analyze_table a on i.image_no = a.image_no
                where i.image_no = ?
                """;
        return jdbcTemplate.queryForObject(sql, new AnalyzeRowMapper(), imageNo);
    }



    public List<AnalyzeDTO> findPaginated(int offset, int limit) {
        String sql = """
                SELECT i. *, a.MEMBER_NO, a.ANALYZE_YEAR, a.GRAPH_TYPE
                From image_table i
                JOIN analyze_table a on i.image_no = a.image_no
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, new AnalyzeRowMapper(), limit, offset);
    }

    public int countAnalyzes() {
        String sql = "SELECT COUNT(*) FROM analyze_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<AnalyzeDTO> findByYear(int year) {
        String sql = "SELECT * FROM analyze_table WHERE analyze_year = ?";
        return jdbcTemplate.query(sql, new AnalyzeRowMapper(), year);
    }


    public void updateAnalyze(Long image_no, AnalyzeDTO analyzeDTO) {
        String sql = "UPDATE analyze_table SET member_no = ?, analyze_year = ?, graph_type = ? " +
                "WHERE image_no = ?";
        jdbcTemplate.update(sql, analyzeDTO.getMember().getMember_no(), analyzeDTO.getAnalyze_year(),
                analyzeDTO.getGraph_type(), analyzeDTO.getImage().getImage_description(), image_no);
    }

}
