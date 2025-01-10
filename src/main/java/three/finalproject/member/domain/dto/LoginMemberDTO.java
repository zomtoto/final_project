package three.finalproject.member.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginMemberDTO {
    private Long member_no; // 회원번호
    private String id; // 아이디
    private String password; // 비밀번호
    private String name;
}
