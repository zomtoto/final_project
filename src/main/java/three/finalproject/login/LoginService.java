package three.finalproject.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import three.finalproject.member.domain.Member;
import three.finalproject.member.domain.dto.MemberDTO;
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
    public MemberDTO login(String id, String password) {
        try {
            // Find the member by ID
            MemberDTO member = memberRepository.findById(id);
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
