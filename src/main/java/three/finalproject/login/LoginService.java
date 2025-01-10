package three.finalproject.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import three.finalproject.member.domain.dto.LoginMemberDTO;
import three.finalproject.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * Handles user login.
     *
     * @param id       The user ID.
     * @param password The user password.
     * @return The `Member` object if login is successful, otherwise `null`.
     */
    public LoginMemberDTO login(String id, String password) {
        try {
            // Find the member by ID
            LoginMemberDTO member = memberRepository.findById(id);
            // Check if the passwords match
            if (member != null && member.getPassword().equals(password)) {
                return member;
            }
        } catch (Exception e) {
            // Handle any errors (e.g., member not found)
            return null;
        }
        return null; // Login failed
    }
}
