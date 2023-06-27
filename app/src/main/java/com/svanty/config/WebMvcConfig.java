package com.svanty.config;

import com.svanty.constans.Language;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();

        messageResource.setBasename("classpath:i18n/general-messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        LocaleResolver resolver = new UrlLocaleResolver();
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        UrlLocaleInterceptor localeInterceptor = new UrlLocaleInterceptor();

        List<String> paths = new ArrayList<>(Arrays.asList(Language.values()))
                .stream().filter(e -> e.enabled).map(e -> "/" + e.languageCode + "/*").collect(Collectors.toList());

        registry
                .addInterceptor(localeInterceptor)
                .addPathPatterns(paths);
    }

}
