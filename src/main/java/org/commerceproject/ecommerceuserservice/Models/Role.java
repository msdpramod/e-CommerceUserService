package org.commerceproject.ecommerceuserservice.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @JsonDeserialize(as = Role.class)
public class Role extends BaseMode{
    private String role;
}
