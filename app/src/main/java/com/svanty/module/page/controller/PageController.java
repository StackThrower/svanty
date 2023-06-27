package com.svanty.module.page.controller;

import com.svanty.module.pages.db.entity.Pages;
import com.svanty.module.pages.service.PagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("{locale}")
public class PageController {

    PagesService pagesService;

    @Autowired
    public PageController(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    @GetMapping("/page/{slug}")
    public String page(@PathVariable String slug, Model model) {

        Pages page = pagesService.findBySlug(slug);

        model.addAttribute("page", page);

        return "module/page/page";
    }




}
