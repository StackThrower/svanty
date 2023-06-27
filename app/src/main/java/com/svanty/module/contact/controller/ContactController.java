package com.svanty.module.contact.controller;

import com.svanty.security.validator.ContactForm;
import com.svanty.security.validator.PasswordResetForm;
import com.svanty.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("{locale}")
public class ContactController {

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/contact")
    public String contact(ContactForm contactForm, Model model) {
        model.addAttribute("isSent", false);
        return "module/contact/contact";
    }

    @PostMapping("/contact")
    public String passwordResetPost(@Valid ContactForm contactForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isSent", false);
                return "module/contact/contact";
        }

        userDetailsService.sendContactRequest(contactForm);

//        userDetailsService.sendPasswordResetRequest(passwordResetForm.getEmail());

        model.addAttribute("isSent", true);

        return "module/contact/contact";
    }




}
