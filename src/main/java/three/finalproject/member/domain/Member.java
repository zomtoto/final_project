package three.finalproject.member.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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



}
