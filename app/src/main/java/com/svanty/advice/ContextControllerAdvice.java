package com.svanty.advice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public class ContextControllerAdvice {

    @Value( "${app.context.url}" )
    private String context;

    @ModelAttribute("static_context")
    public String locale() {
        return context;
    }
}
