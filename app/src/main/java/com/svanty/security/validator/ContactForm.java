package com.svanty.security.validator;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ContactForm {

    @NotNull
    @NotEmpty(message = "{validation.form.name.empty}")
    private String name;

    @NotNull
    @Email(message = "{validation.form.email.format}")
    @NotEmpty(message = "{validation.form.email.empty}")
    private String email;

    @NotNull
    @NotEmpty(message = "{validation.form.subject.empty}")
    private String subject;

    @NotNull
    @NotEmpty(message = "{validation.form.message.empty}")
    private String message;
}
