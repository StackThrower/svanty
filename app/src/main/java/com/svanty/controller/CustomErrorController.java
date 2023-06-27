package com.svanty.controller;

import com.svanty.constans.GlobalConstants;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class CustomErrorController implements ErrorController {

    Locale systemLocale = LocaleContextHolder.getLocale();

    @GetMapping("/error")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Locale locale = (Locale) request.getSession().getAttribute(GlobalConstants.URL_LOCALE_ATTRIBUTE_NAME);
        String langCode = "en";

        if(locale == null) {
            if(systemLocale != null)
                langCode = systemLocale.getLanguage();
        } else {
            langCode = locale.getLanguage();
        }
        model.addAttribute("locale", langCode);

        switch (Integer.parseInt(status.toString())) {
            case 404:
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return "error-404";
            case 401:
            case 403:
                return "redirect:/" + langCode + "/signin";
            case 500:
            default:
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return "error-500";
        }


    }
}
