package org.commerceproject.ecommerceuserservice.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.commerceproject.ecommerceuserservice.DTOs.UserDTO;
import org.commerceproject.ecommerceuserservice.Models.Role;
import org.commerceproject.ecommerceuserservice.Models.Session;
import org.commerceproject.ecommerceuserservice.Models.SessionStatus;
import org.commerceproject.ecommerceuserservice.Models.User;
import org.commerceproject.ecommerceuserservice.Repository.SessionRepository;
import org.commerceproject.ecommerceuserservice.Repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService implements AuthServiceInterface{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public ResponseEntity<UserDTO> login(String email, String password) {
        // find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        // if user is not found
        // if password is not correct
        if(userOptional.isEmpty()){
            return null;
        }
        // if user is found
        User user = userOptional.get();
        // bCryptPasswordEncoder.matches(password, user.getPassword()): This part of the code is using the matches method of BCryptPasswordEncoder to compare the provided plain-text password (password) with the hashed password stored in the User object (user.getPassword()). The matches method returns true if the two passwords match, and false otherwise.


        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        // generate token by using JWT library and return it in response header with user details in response body as JSON object (DTO)
        /*The code you provided generates a random alphanumeric string of length 128 using the RandomStringUtils class
         from Apache Commons Lang library. This kind of random string generation is often used for creating unique tokens,
          such as authentication tokens or session identifiers. */


        String token= RandomStringUtils.randomAlphanumeric(128);


        // HS256 is the algorithm used to sign the token and can be changed to HS384 or HS512 as per requirement
        /*
            It looks like you are working with JSON Web Tokens (JWTs) and specifying the signing algorithm as HS256. The code snippet you provided seems to be using a library that supports JWTs, and it's setting the signing algorithm to HMAC SHA-256 (HS256).
            However, there is a small mistake in the code. The correct syntax for specifying the HMAC SHA-256 algorithm in
            JWT libraries is typically like this:
         */


        MacAlgorithm macAlgorithm= Jwts.SIG.HS256; //or HS384 or HS256


        // secret key is used to sign the token and can be changed as per requirement (it should be stored in environment variable) and should be of length 256 bits


        SecretKey secretKey= macAlgorithm.key().build();


//        String message = "{\n" +
//                "   \"email\": \"naman@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"mentor\",\n" +
//                "      \"ta\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"23rdOctober2023\"\n" +
//                "}";
//        // user_id
//        // user_email
//        // roles
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

// Create the compact JWS:

        Map<String, Object> jsonForJwt = new HashMap<>();
        jsonForJwt.put("email", user.getEmail());
//        jsonForJwt.put("roles", user.getRoles());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));


        token = Jwts.builder()
                .claims(jsonForJwt)
                .signWith(secretKey, macAlgorithm)
                .compact();

        //
//compact// Parse the compact JWS:
//        content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();

        Session session = new Session();
        session.setStatus(SessionStatus.Active);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDTO userDto = UserDTO.from(user);

        /*
            It looks like you are working with HTTP headers and setting a "Set-Cookie" header in Java. The code snippet you
             provided is using a MultiValueMapAdapter to handle HTTP headers. Assuming you are using
            Spring Framework or a similar library that utilizes MultiValueMap for representing HTTP headers,
         */

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDTO> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return response;

    }

    @Override
    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setStatus(SessionStatus.Inactive);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    @Override
    public UserDTO signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        return UserDTO.from(savedUser);
    }

    @Override
    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.Inactive;
        }

        Session session = sessionOptional.get();

        if (!session.getStatus().equals(SessionStatus.Active)) {
            return SessionStatus.Inactive;
        }


        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);

        String email = (String) claimsJws.getPayload().get("email");
        List<Role> roles = (List<Role>) claimsJws.getPayload().get("roles");
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");

        if (createdAt.before(new Date())) {
            return SessionStatus.Inactive;
        }


//        if (!session.)

        return SessionStatus.Active;
    }
}
