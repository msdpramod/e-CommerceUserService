package org.commerceproject.ecommerceuserservice.Service;

import org.commerceproject.ecommerceuserservice.DTOs.UserDTO;
import org.commerceproject.ecommerceuserservice.Models.SessionStatus;
import org.springframework.http.ResponseEntity;

public interface AuthServiceInterface {
    ResponseEntity<UserDTO> login(String email, String password);
    ResponseEntity<Void> logout(String token, Long userId);
    ResponseEntity<UserDTO> signUp(String email, String password);
    ResponseEntity<SessionStatus> validate(String token, Long userId);

}
