package org.commerceproject.ecommerceuserservice.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurity {
    //    @Bean
//    @Order(1)
//    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception {
//        http.cors().disable();
//        http.csrf().disable();
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
////        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
////        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/*").authenticated());
//        return http.build();
//    }
    // Object that handles what all api endpoints should be authenticated
    // v/s what all shouldn't be authenticated

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
