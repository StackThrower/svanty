package com.svanty.module.stock.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller
public class RedirectController {

    /**
     * Redirect for photo
     * @param photoId
     * @param slug
     * @return
     */
    @GetMapping("/photo/{photoId}/{slug}")
    String photoRedirect(@PathVariable Integer photoId, @PathVariable String slug) {
        Locale locale = LocaleContextHolder.getLocale();

        slug = URLEncoder.encode(slug, StandardCharsets.UTF_8);

        return "redirect:/" + locale.getLanguage() + "/photo/" + photoId + "/" + slug;
    }

    /**
     * Redirect for tags
     * @param color
     * @return
     */
    @GetMapping("/colors/{color}")
    String colorRedirect(@PathVariable String color) {
        Locale locale = LocaleContextHolder.getLocale();

        color = URLEncoder.encode(color, StandardCharsets.UTF_8);

        return "redirect:/" + locale.getLanguage() + "/colors/" + color;
    }

    /**
     * Redirect for camera
     * @param camera
     * @return
     */
    @GetMapping("/cameras/{camera}")
    String camerasRedirect(@PathVariable String camera) {
        Locale locale = LocaleContextHolder.getLocale();

        camera = URLEncoder.encode(camera, StandardCharsets.UTF_8);

        return "redirect:/" + locale.getLanguage() + "/cameras/" + camera;
    }

    /**
     * Redirect for category
     * @param category
     * @return
     */
    @GetMapping("/category/{category}")
    String categoryRedirect(@PathVariable String category) {
        Locale locale = LocaleContextHolder.getLocale();

        category = URLEncoder.encode(category, StandardCharsets.UTF_8);

        return "redirect:/" + locale.getLanguage() + "/category/" + category;
    }

    /**
     * Redirect for page
     * @param slug
     * @return
     */
    @GetMapping("/page/{slug}")
    String pageRedirect(@PathVariable String slug) {
        Locale locale = LocaleContextHolder.getLocale();

        slug = URLEncoder.encode(slug, StandardCharsets.UTF_8);

        return "redirect:/" + locale.getLanguage() + "/page/" + slug;
    }



}
