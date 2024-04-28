package rs.raf.userbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.userbackend.model.MyUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser,Long> {
    Optional<MyUser> findUserByUsernameAndPassword(String username, String password);
    Optional<MyUser> findUserByUsername(String username);
    Optional<MyUser> findUserByUserId(Long id);
}
