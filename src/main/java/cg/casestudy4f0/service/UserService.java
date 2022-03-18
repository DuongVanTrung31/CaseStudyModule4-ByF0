package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends GenericService<User>, UserDetailsService {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
