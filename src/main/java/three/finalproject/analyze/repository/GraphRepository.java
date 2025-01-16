package three.finalproject.analyze.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.analyze.domain.dto.GraphDTO;
import three.finalproject.analyze.domain.dto.YearDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GraphRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper for GraphDTO
    private static class GraphRowMapper implements RowMapper<GraphDTO> {
        @Override
        public GraphDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            GraphDTO graph = new GraphDTO();
            graph.setGraph_type(rs.getString("graph_type"));
            return graph;
        }
    }

    //Find all year_no in year_table
    public List<GraphDTO> findAllGraphs() {
        String sql = """
                SELECT graph_name as graph_type
                FROM graph_table
                """;
        return jdbcTemplate.query(sql,new GraphRowMapper());
    }
}
