package three.finalproject.domain.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to map result set rows to Member objects
    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
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
        member.setDelete(rs.getString("delete"));
        return member;
    };

    public Member save(Member member) {
        String sql = "INSERT INTO member_table (id, password, name, dob, gender, email, phone, admin, joinDate, delete) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'N', CURRENT_DATE, 'False')";
        jdbcTemplate.update(sql, member.getId(), member.getPassword(), member.getName(),
                member.getDob(), member.getGender(), member.getEmail(), member.getPhone());

        // Use MAX(member_no) to retrieve the latest inserted ID
        Long member_no = jdbcTemplate.queryForObject("SELECT MAX(member_no) FROM member_table", Long.class);
        member.setMember_no(member_no);

        return member;
    }


    public Member findByNo(Long member_no) {
        String sql = "SELECT * FROM member_table WHERE member_no = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, member_no);
    }

    public Member findById(String id) {
        String sql = "SELECT * FROM member_table WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM member_table";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
    public void update(Long member_no, Member updatedMember) {
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

    public List<Member> findWithPagination(int offset, int size) {
        String sql = "SELECT * FROM member_table ORDER BY member_no ASC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, memberRowMapper);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM member_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // Fetch paginated members
    public List<Member> findPaginated(int offset, int limit) {
        String sql = "SELECT * FROM member_table ORDER BY member_no LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{limit, offset}, (rs, rowNum) -> {
            Member member = new Member();
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
    public List<Member> findByJoinYear(String year) {
        String sql = "SELECT * FROM member_table WHERE delete = 'F' AND joinDate LIKE ? ORDER BY member_no";
        return jdbcTemplate.query(sql, memberRowMapper, year + "%");
    }


}
