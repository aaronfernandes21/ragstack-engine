package com.aaron.aiDoc.dto;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
public class LoginDto {

    private String email;
    private String password;
}
