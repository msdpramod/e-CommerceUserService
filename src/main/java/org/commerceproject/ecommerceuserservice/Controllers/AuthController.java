package org.commerceproject.ecommerceuserservice.Controllers;

import org.commerceproject.ecommerceuserservice.DTOs.*;
import org.commerceproject.ecommerceuserservice.Models.SessionStatus;
import org.commerceproject.ecommerceuserservice.Service.AuthServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthServiceInterface authServiceInterface;

    public AuthController(AuthServiceInterface authServiceInterface) {
        this.authServiceInterface = authServiceInterface;
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequest){
        return authServiceInterface.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO request) {
        return authServiceInterface.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO request) {
        UserDTO userDto = authServiceInterface.signUp(request.getEmail(), request.getPassword()).getBody();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

//    @GetMapping("/signup")
//    public String signUp() {
////        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
//        return "hello";
//    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(ValidateTokenRequestDTO request) {
        SessionStatus sessionStatus = authServiceInterface.validate(request.getToken(), request.getUserId()).getBody();
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
}
