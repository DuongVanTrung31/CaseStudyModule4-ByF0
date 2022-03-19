package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
