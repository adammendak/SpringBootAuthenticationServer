package com.adammendak.authentication.model;

import lombok.Data;
import javax.persistence.Entity;


@Entity
@Data
public class User {

    private Long id;
    private String login;
    private String password;
}
