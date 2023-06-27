package com.svanty.advice;

import com.svanty.constans.GlobalConstants;
import com.svanty.db.page.Paged;
import com.svanty.module.pages.db.entity.Pages;
import com.svanty.module.pages.service.PagesService;
import com.svanty.module.stock.model.CategoryItemModel;
import com.svanty.module.stock.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class PagesControllerAdvice {

    private PagesService pagesService;

    @Autowired
    PagesControllerAdvice(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    @ModelAttribute("pages")
    public Paged<Pages> getPages() {

        Paged<Pages> pages = pagesService.findAll(0, GlobalConstants.APP_PAGE_PAGES_MAX_COUNT);

        return pages;
    }
}
