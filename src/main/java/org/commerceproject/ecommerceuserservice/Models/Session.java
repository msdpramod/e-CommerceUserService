package org.commerceproject.ecommerceuserservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity @Setter @Getter
public class Session extends BaseMode{
    private String token;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;
    @ManyToOne
    private User user;

    private Date expiryDate;
}
