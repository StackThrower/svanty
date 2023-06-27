package com.svanty.security.validator;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PasswordResetForm {
    @NotNull
    @Email(message = "{validation.form.email.format}")
    @NotEmpty(message = "{validation.form.email.empty}")
    private String email;

}
