package three.finalproject.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty
    private String member_id;
    @NotEmpty
    private String member_password;

}
