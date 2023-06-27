package com.svanty.security.validator;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
public class AccountSettingsForm {

    @NotNull
    @Size(min=3, max=100, message = "{validation.form.name.size}")
    private String name;

    @NotNull
    @Size(min=3, max=30, message = "{validation.form.username.size}")
    private String username;

    @NotNull
    @Email(message = "{validation.form.email.format}")
    @NotEmpty(message = "{validation.form.email.empty}")
    private String email;

    private String city;

    private String paypal_account;

    private String website;

    private String facebook;

    private String twitter;

    private String instagram;

    private String bio;

    private MultipartFile avatar;

    private MultipartFile background;
}
