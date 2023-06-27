package com.svanty.security.validator;


import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignInForm {

    @NotNull
    @Email(message = "{validation.form.email.format}")
    @NotEmpty(message = "{validation.form.email.empty}")
    private String email;

    @NotNull
    @NotEmpty(message = "{validation.form.password.empty}")
    private String password;
}
