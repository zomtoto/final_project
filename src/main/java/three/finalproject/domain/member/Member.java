package three.finalproject.domain.member;

import lombok.Data;

@Data
public class Member {
    private Long member_no; // 회원번호
    private String id; // 아이디
    private String password; // 비밀번호
    private String name; //이름
    private String dob; //생년월일 (YYYY-MM-DD)
    private String gender; // 성별 '남' '여'
    private String email; //이메일
    private String phone; //전화번호
    private String admin; //관리자 여부
    private String joinDate; //회원가입일 (YYYY-MM--DD)
    private String delete; //삭제 여부

    public Member() {
    }

    public Member(Long member_no, String id, String password, String name, String dob, String gender,
                  String email, String phone, String admin, String joinDate, String delete) {
        this.member_no = member_no;
        this.id = id;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.admin = admin;
        this.joinDate = joinDate;
        this.delete = delete;
    }
}
