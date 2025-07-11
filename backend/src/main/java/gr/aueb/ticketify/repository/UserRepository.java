package gr.aueb.ticketify.repository;

import gr.aueb.ticketify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
//    Optional<User> findByIdAndIsActiveTrue(Long id);
//    List<User> findAllByIsActiveTrue();
    boolean existsByUsername(String username);
}
