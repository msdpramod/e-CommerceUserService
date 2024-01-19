package org.commerceproject.ecommerceuserservice.Repository;

import org.commerceproject.ecommerceuserservice.Models.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SessionRepository {
    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
    Session save(Session session);
}
