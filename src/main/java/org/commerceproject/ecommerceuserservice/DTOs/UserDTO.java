package org.commerceproject.ecommerceuserservice.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.commerceproject.ecommerceuserservice.Models.Role;
import org.commerceproject.ecommerceuserservice.Models.User;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class UserDTO {
    private String Email;
    private Set<Role> roles= new HashSet<>();

    public static UserDTO from(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
//        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
