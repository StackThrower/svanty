package com.svanty.security.validator;


import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignUpForm {

    @NotNull
    @Size(min=3, max=30, message = "{validation.form.username.size}")
    private String username;

    @NotNull
    @Email(message = "{validation.form.email.format}")
    @NotEmpty(message = "{validation.form.email.empty}")
    private String email;

    @NotNull
    @Size(min=6, max=20, message = "{validation.form.password.size}")
    private String password;

    @NotNull
    @Size(min=6, max=20, message = "{validation.form.password-confirmation.size}")
    private String passwordConfirmation;

    @AssertTrue(message = "{validation.form.agreement.true}")
    private Boolean agreement;
}
