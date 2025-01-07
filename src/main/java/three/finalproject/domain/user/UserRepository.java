package three.finalproject.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Repository
public class UserRepository {

    private static final Map<Long, User> store = new HashMap<>(); //static 사용
    private static long sequence = 0L;

    public User save(User user) {
        user.setId(++sequence);
        log.info("save user:{}", user);
        store.put(user.getId(), user);

        return user;
    }

    public User findById(Long id) {
        return store.get(id);
    }

    public Optional<User> findByUserId(String user_id) {
        return findAll().stream()
                .filter(u -> u.getUser_id().equals(user_id))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
