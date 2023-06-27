package com.svanty.module.core.controller;

import com.svanty.constans.GlobalConstants;
import com.svanty.module.core.db.UserRepository;
import com.svanty.module.core.db.entity.User;
import com.svanty.security.validator.PasswordResetForm;
import com.svanty.security.validator.SignInForm;
import com.svanty.security.validator.SignUpForm;
import com.svanty.service.EncryptionDecryptionService;
import com.svanty.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("{locale}")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    EncryptionDecryptionService encryptionDecryptionService;


    @GetMapping("/signin")
    public String signin(SignInForm signInForm) {
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String signUp(SignUpForm signUpForm) {
        return "auth/signup";
    }

    @GetMapping("/password/reset")
    public String passwordResetGet(PasswordResetForm passwordResetForm, Model model) {
        model.addAttribute("isSent", false);
        return "auth/password-reset";

    }

    @PostMapping("/password/reset")
    public String passwordResetPost(@Valid PasswordResetForm passwordResetForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isSent", false);
            return "auth/password-reset";
        }

        userDetailsService.sendPasswordResetRequest(passwordResetForm.getEmail());

        model.addAttribute("isSent", true);

        return "auth/password-reset";
    }

    @GetMapping("/password/reset/{code}")
    public String passwordReset(@PathVariable String code, HttpServletRequest request) {

        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);

        String email = encryptionDecryptionService.decrypt(code, EncryptionDecryptionService.DEFAULT_SECRET);

        try {
            User user = userRepository.findByAuth(email).stream().findFirst().orElseThrow();

            if (user.getStatus().equals(User.Status.active) &&
                    (user.getModified() == null || user.getModified().getTime() < new Date().getTime())) {

                final String password = userDetailsService.generateRandomPassword(UserDetailsService.DEFAULT_PASSWORD_LENGTH);

                user.setPassword(passwordEncoder.encode(password));
                user.setModified(new Date(System.currentTimeMillis() + User.MODIFICATION_TIME_OUT));

                userDetailsService.sendPasswordResetEmail(user.getEmail(), password);

                userRepository.save(user);
            }

        } finally {

            return "redirect:/" + locale.getLanguage() + "/signin";
        }
    }


    private ReloadableResourceBundleMessageSource getValidationMessages() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:i18n/validation-messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @PostMapping("/signup")
    public String addUser(@Valid SignUpForm signUpForm, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);
        ReloadableResourceBundleMessageSource validationMessages = getValidationMessages();

        if (userRepository.findByAuth(signUpForm.getEmail()).size() > 0) {

            String errorText = validationMessages.getMessage("validation.form.email.exists", null, locale);
            bindingResult.rejectValue("email",
                    "validation.form.email.exists", errorText);

            return "auth/signup";
        }

        if (!signUpForm.getPassword().equals(signUpForm.getPasswordConfirmation())) {

            String errorText = validationMessages.getMessage("validation.form.password.not-match-re-password", null, locale);
            bindingResult.rejectValue("password",
                    "validation.form.password.not-match-re-password", errorText);

            return "auth/signup";
        }

        User user = new User();

        user.setUsername(signUpForm.getUsername());
        user.setName(signUpForm.getUsername());
        user.setBio("Photographer");
        user.setCountries_id(String.valueOf(0));
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        user.setEmail(signUpForm.getEmail());
        user.setDate(new Date());
        user.setModified(new Date());
        user.setAvatar("default.jpg");
        user.setCover("cover.jpg");
        user.setStatus(User.Status.pending);
        user.setTypeAccount(User.TypeAccount.seller);
        user.setRole(User.Role.normal);
        user.setWebsite("");
        user.setRemember_token("");
        user.setTwitter("");
        user.setFacebook("");
        user.setGoogle("");
        user.setPaypal_account("");
        user.setActivation_code("");
        user.setOauth_uid("");
        user.setOauth_provider("");
        user.setToken("");
        user.setAuthorizedToUpload(User.AuthorizedToUpload.no);
        user.setInstagram("");
        user.setFunds(BigDecimal.valueOf(0));
        user.setBalance(BigDecimal.valueOf(0));
        user.setPaymentGateway("");
        user.setBank("");
        user.setIp(request.getRemoteAddr());
        user.setAuthorExclusive(User.AuthorExclusive.no);

        userRepository.save(user);

        userDetailsService.sendConfirmationEmail(user.getEmail());

        return "redirect:/" + locale.getLanguage() + "/signin";
    }


    @PostMapping("/signin")
    public String signin(@Valid SignInForm signInForm, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "auth/signin";
        }

        ReloadableResourceBundleMessageSource validationMessages = getValidationMessages();
        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);

        Authentication authentication;
        try {
            UserDetails principal = userDetailsService.loadUserByUsername(signInForm.getEmail());
            authentication = new UsernamePasswordAuthenticationToken(principal, signInForm.getPassword(), principal.getAuthorities());

            authenticationManager.authenticate(authentication);

        } catch (AuthenticationException e) {
            String errorText = validationMessages.getMessage("validation.form.auth.not-correct", null, locale);
            bindingResult.rejectValue("email",
                    "validation.form.auth.not-correct", errorText);

            return "auth/signin";
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication.isAuthenticated() ? "redirect:/" + locale.getLanguage() + "/" :
                "redirect:/" + locale.getLanguage() + "/signin";
    }

    @GetMapping("/logout")
    public String signUp(HttpServletRequest request) {
        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/" + locale.getLanguage() + "/";
    }

    @GetMapping("/verify/account/{code}")
    public String verifyAccount(@PathVariable String code, HttpServletRequest request) {
        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);

        String email = encryptionDecryptionService.decrypt(code, EncryptionDecryptionService.DEFAULT_SECRET);

        try {
            User user = userRepository.findByAuth(email).stream().findFirst().orElseThrow();
            if (user.getStatus().equals(User.Status.pending)) {

                user.setStatus(User.Status.active);
                userRepository.save(user);
            }

        } finally {

            return "redirect:/" + locale.getLanguage() + "/signin";
        }
    }

}
