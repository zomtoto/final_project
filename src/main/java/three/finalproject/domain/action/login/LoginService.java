package three.finalproject.domain.action.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import three.finalproject.domain.member.Member;
import three.finalproject.domain.member.MemberRepository;

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
    public Member login(String id, String password) {
        try {
            // Find the member by ID
            Member member = memberRepository.findById(id);
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
