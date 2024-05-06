package chunyin.backendUsersPokedex.dao;

import chunyin.backendUsersPokedex.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDAO extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
