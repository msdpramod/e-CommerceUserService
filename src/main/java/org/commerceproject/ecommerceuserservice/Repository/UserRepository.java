package org.commerceproject.ecommerceuserservice.Repository;

import org.commerceproject.ecommerceuserservice.Models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
