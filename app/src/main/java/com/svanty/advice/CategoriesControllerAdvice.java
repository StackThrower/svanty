package com.svanty.advice;

import com.svanty.constans.GlobalConstants;
import com.svanty.constans.Language;
import com.svanty.module.stock.model.CategoryItemModel;
import com.svanty.module.stock.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class CategoriesControllerAdvice {

    private CategoriesService categoriesService;

    @Autowired
    CategoriesControllerAdvice(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @ModelAttribute("categories")
    public List<CategoryItemModel> getCategories() {

        List<CategoryItemModel> categories = categoriesService
                .getCategoryItemPageList(GlobalConstants.APP_PAGE_CATEGORIES_TOP_MENU_LIST_COUNT);

        return categories;
    }
}
