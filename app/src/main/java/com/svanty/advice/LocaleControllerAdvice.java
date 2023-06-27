package com.svanty.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public class LocaleControllerAdvice {

    private static final String URL_LOCALE_ATTRIBUTE_NAME = "URL_LOCALE_ATTRIBUTE_NAME";

    @ModelAttribute("locale")
    public String locale(HttpServletRequest request) {

        Locale locale = (Locale) request.getSession().getAttribute(URL_LOCALE_ATTRIBUTE_NAME);

        return locale != null ? locale.getLanguage() : "en";
    }
}
