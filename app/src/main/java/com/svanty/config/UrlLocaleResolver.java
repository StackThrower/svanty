package com.svanty.config;

import com.svanty.constans.GlobalConstants;
import com.svanty.constans.Language;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class UrlLocaleResolver implements LocaleResolver {



    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String uri = request.getRequestURI();
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()))
                .stream().filter(e -> e.enabled).collect(Collectors.toList());

        Locale locale = null;

        for(Language language : languages) {
            if (uri.startsWith("/" + language.languageCode + "/")) {
                locale = new Locale(language.languageCode, language.countryCode);
            }
        }

        if (locale != null) {
            request.getSession().setAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME, locale);
        }
        if (locale == null) {
            locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // Nothing
    }

}