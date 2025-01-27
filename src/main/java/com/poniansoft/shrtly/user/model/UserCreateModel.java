package com.poniansoft.shrtly.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateModel {
    @NotBlank(message = "UID cannot be blank")
    String uid;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    String email;
}
