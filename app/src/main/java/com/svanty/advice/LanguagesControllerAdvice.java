package com.svanty.advice;

import com.svanty.constans.Language;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class LanguagesControllerAdvice {

    @ModelAttribute("languages")
    public List<Language> locale() {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));

        return languages;
    }
}
