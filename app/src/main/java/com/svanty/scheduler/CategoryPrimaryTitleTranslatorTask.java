package com.svanty.scheduler;

import com.svanty.module.stock.service.CategoriesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CategoryPrimaryTitleTranslatorTask {

    private CategoriesService categoriesService;

    CategoryPrimaryTitleTranslatorTask(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }


    @Scheduled(fixedDelay = 60, initialDelay = 60, timeUnit = TimeUnit.MINUTES)
    void translateCategories() {

        categoriesService.translateAllCategoriesWithoutTranslation();
    }

}
