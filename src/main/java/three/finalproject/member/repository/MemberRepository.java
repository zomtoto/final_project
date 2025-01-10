package three.finalproject.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import three.finalproject.member.domain.dto.LoginMemberDTO;
import three.finalproject.member.domain.dto.MemberDTO;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper to map result set rows to Member objects
    private final RowMapper<MemberDTO> memberRowMapper = (rs, rowNum) -> {
        MemberDTO member = new MemberDTO();
        member.setMember_no(rs.getLong("member_no"));
        member.setId(rs.getString("id"));
        member.setPassword(rs.getString("password"));
        member.setName(rs.getString("name"));
        member.setDob(rs.getString("dob"));
        member.setGender(rs.getString("gender"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setAdmin(rs.getString("admin"));
        member.setJoinDate(rs.getString("joinDate"));
        member.setDelete(rs.getBoolean("delete"));
        return member;
    };
    private final RowMapper<LoginMemberDTO> loginRowMapper = (rs, rowNum) -> {
        LoginMemberDTO loginMember = new LoginMemberDTO();
        loginMember.setMember_no(rs.getLong("member_no"));
        loginMember.setId(rs.getString("id"));
        loginMember.setPassword(rs.getString("password"));
        loginMember.setName(rs.getString("name"));
        return loginMember;
    };

    public MemberDTO save(MemberDTO member) {
        String sql = "INSERT INTO member_table (id, password, name, dob, gender, email, phone, admin, joinDate, delete) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'N', CURRENT_DATE, 'False')";
        jdbcTemplate.update(sql, member.getId(), member.getPassword(), member.getName(),
                member.getDob(), member.getGender(), member.getEmail(), member.getPhone());

        // Use MAX(member_no) to retrieve the latest inserted ID
        Long member_no = jdbcTemplate.queryForObject("SELECT MAX(member_no) FROM member_table", Long.class);
        member.setMember_no(member_no);

        return member;
    }


    public LoginMemberDTO findByNo(Long member_no) {
        String sql = "SELECT * FROM member_table WHERE member_no = ?";
        return jdbcTemplate.queryForObject(sql, loginRowMapper, member_no);
    }

    public LoginMemberDTO findById(String id) {
        String sql = "SELECT * FROM member_table WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, loginRowMapper, id);
    }

    public List<MemberDTO> findAll() {
        String sql = "SELECT * FROM member_table";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
    public void update(Long member_no, MemberDTO updatedMember) {
        String sql = "UPDATE member_table SET id = ?, password = ?, name = ?, dob = ?, gender = ?, " +
                "email = ?, phone = ?, admin = ?, delete = ? WHERE member_no = ?";
        jdbcTemplate.update(sql, updatedMember.getId(), updatedMember.getPassword(), updatedMember.getName(),
                updatedMember.getDob(), updatedMember.getGender(), updatedMember.getEmail(),
                updatedMember.getPhone(), updatedMember.getAdmin(), updatedMember.getDelete(), member_no);
    }

    public void delete(Long member_no) {
        String sql = "UPDATE member_table SET delete = 'T' WHERE member_no = ?";
        jdbcTemplate.update(sql, member_no);
    }

    public List<MemberDTO> findWithPagination(int offset, int size) {
        String sql = "SELECT * FROM member_table ORDER BY member_no ASC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, memberRowMapper);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM member_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // Fetch paginated members
    public List<MemberDTO> findPaginated(int offset, int limit) {
        String sql = "SELECT * FROM member_table ORDER BY member_no LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{limit, offset}, (rs, rowNum) -> {
            MemberDTO member = new MemberDTO();
            member.setMember_no(rs.getLong("member_no"));
            member.setId(rs.getString("id"));
            member.setName(rs.getString("name"));
            member.setGender(rs.getString("gender"));
            member.setDob(rs.getString("dob"));
            return member;
        });
    }
    // Count total members
    public int countMembers() {
        String sql = "SELECT COUNT(*) FROM member_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // Get unique years from joinDate
    public List<String> findUniqueYears() {
        String sql = "SELECT DISTINCT SUBSTRING(joinDate, 1, 4) AS joinYear FROM member_table WHERE delete = 'F' ORDER BY joinYear ASC";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Find members by joinDate year
    public List<MemberDTO> findByJoinYear(String year) {
        String sql = "SELECT * FROM member_table WHERE delete = 'F' AND joinDate LIKE ? ORDER BY member_no";
        return jdbcTemplate.query(sql, memberRowMapper, year + "%");
    }


}
