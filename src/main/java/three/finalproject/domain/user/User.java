package three.finalproject.domain.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id; //인덱스


    private Integer user_code; //유저 코드
    @NotEmpty
    private String user_id; //유저 로그인Id
    @NotEmpty
    private String user_password; //유저비밀번호
    @NotEmpty
    private String user_name;
    @NotEmpty
    private String jumin;
    private String address;
    private String pay_method;
    private String email_address;
    private Integer mileage;
    private Boolean manager_check;
    private Date user_sign_up;

}
