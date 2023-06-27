package com.svanty.service;

import com.svanty.model.Mail;
import com.svanty.module.core.db.UserRepository;
import com.svanty.module.core.db.entity.User;
import com.svanty.security.UserDetailsSecurity;
import com.svanty.security.validator.ContactForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    public final static int DEFAULT_PASSWORD_LENGTH = 8;

    @Autowired
    MessageSource messageSource;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    EncryptionDecryptionService encryptionDecryptionService;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findByAuth(username);
        if (users == null || users.size() == 0 || !users.stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not active"))
                .getStatus().equals(User.Status.active)) {
            throw new UsernameNotFoundException("User not found.");
        }
        log.info("loadUserByUsername() : {}", username);
        return new UserDetailsSecurity(users.get(0));
    }


    public void sendConfirmationEmail(String email) {

        Locale locale = LocaleContextHolder.getLocale();

        Mail mail = new Mail();
        mail.setFrom("support@svanty.com");
        mail.setMailTo(email);
        mail.setSubject(messageSource.getMessage("email.verify_subject", new Object[0], locale));
        mail.setTemplate("email/verify");
        mail.setLocale(locale);
        Map<String, Object> model = new HashMap<>();

        String encryptedCode = encryptionDecryptionService.encrypt(email, EncryptionDecryptionService.DEFAULT_SECRET);

        model.put("confirmCode", encryptedCode);
        model.put("verify_url", String.format("https://svanty.com/%s/verify/account/%s", locale.getLanguage(), encryptedCode));
        model.put("locale", locale.getLanguage());

        mail.setProps(model);
        try {
            emailSenderService.sendEmail(mail);
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendPasswordResetRequest(String email) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            User user = userRepository.findByAuth(email).stream().findFirst().orElseThrow();

            Mail mail = new Mail();
            mail.setFrom("support@svanty.com");
            mail.setMailTo(email);
            mail.setSubject(messageSource.getMessage("email.password_reset_request_subject", new Object[0], locale));
            mail.setTemplate("email/password-reset");
            mail.setLocale(locale);
            Map<String, Object> model = new HashMap<>();

            String encryptedCode = encryptionDecryptionService.encrypt(email, EncryptionDecryptionService.DEFAULT_SECRET);

            model.put("reset_url", String.format("https://svanty.com/%s/password/reset/%s", locale.getLanguage(), encryptedCode));
            model.put("locale", locale.getLanguage());

            mail.setProps(model);

            user.setModified(new Date());
            userRepository.save(user);
            try {
                emailSenderService.sendEmail(mail);
            } catch (MessagingException | IOException e) {
                log.error(e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


    public String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                + "jklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }


    public void sendPasswordResetEmail(String email, String password) {
        Locale locale = LocaleContextHolder.getLocale();

        Mail mail = new Mail();
        mail.setFrom("support@svanty.com");
        mail.setMailTo(email);
        mail.setSubject(messageSource.getMessage("email.password_reset_subject", new Object[0], locale));
        mail.setTemplate("email/password-reset-with-password");
        mail.setLocale(locale);
        Map<String, Object> model = new HashMap<>();

        model.put("password", password);
        model.put("locale", locale.getLanguage());

        mail.setProps(model);
        try {
            emailSenderService.sendEmail(mail);
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage());
        }


    }

    public void sendContactRequest(ContactForm contactForm) {

        Locale locale = LocaleContextHolder.getLocale();

        Mail mail = new Mail();
        mail.setFrom("support@svanty.com");
        mail.setMailTo("support@svanty.com");
        mail.setSubject(contactForm.getSubject());
        mail.setTemplate("email/contact-request");
        mail.setLocale(locale);
        Map<String, Object> model = new HashMap<>();

        model.put("contact_email", contactForm.getEmail());
        model.put("contact_name", contactForm.getName());
        model.put("contact_message", contactForm.getMessage());

        model.put("locale", locale.getLanguage());

        mail.setProps(model);
        try {
            emailSenderService.sendEmail(mail);
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage());
        }

    }
}
