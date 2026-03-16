package com.edouard.splitwise_lite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String displayName;
    private String password;
}