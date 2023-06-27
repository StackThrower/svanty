package com.svanty.module.core.controller;

import com.svanty.constans.GlobalConstants;
import com.svanty.db.page.Paged;
import com.svanty.module.blog.model.BlogPostGroupModel;
import com.svanty.module.blog.model.BlogPostItemModel;
import com.svanty.module.blog.service.BlogPostsService;
import com.svanty.module.stock.model.CategoryGroupModel;
import com.svanty.module.stock.model.CategoryItemModel;
import com.svanty.module.stock.model.ImageItemModel;
import com.svanty.module.stock.model.StatisticGeneralModel;
import com.svanty.module.stock.service.CategoriesService;
import com.svanty.module.stock.service.ImagesService;
import com.svanty.module.stock.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
public class IndexController {

    private ImagesService imagesService;
    private CategoriesService categoriesService;
    private BlogPostsService blogPostsService;
    private StatisticService statisticService;

    @Autowired
    IndexController(ImagesService imagesService, CategoriesService categoriesService,
                    BlogPostsService blogPostsService, StatisticService statisticService) {
        this.imagesService = imagesService;
        this.categoriesService = categoriesService;
        this.blogPostsService = blogPostsService;
        this.statisticService = statisticService;
    }


    Locale systemLocale = LocaleContextHolder.getLocale();

    /**
     * Main page
     * @param locale
     * @param model
     * @return
     */
    @GetMapping("/{locale}/")
    public String indexWithLocale(@PathVariable("locale") String locale, Model model) {

        if (locale == null || locale.length() != 2)
            return "redirect:/" + systemLocale.getLanguage() + "/";

        Paged<ImageItemModel> featured = imagesService
                .getFeaturedImageItemPage(GlobalConstants.INDEX_PAGE_FEATURED_ITEMS_START,
                GlobalConstants.INDEX_PAGE_FEATURED_ITEMS_COUNT);

        List<CategoryItemModel> categories = categoriesService
                .getCategoryItemPageListWithCounts(GlobalConstants.INDEX_PAGE_CATEGORIES_MAX_LIST_COUNT);

        List<CategoryGroupModel> categoryGroups = categoriesService.createGroups(categories);

        List<CategoryItemModel> mostViewedCategories =  categoriesService.getMostViewed(categories,
                GlobalConstants.INDEX_PAGE_CATEGORIES_MOST_VIEWED_COUNT);

        List<BlogPostItemModel> blogPostsItems = blogPostsService.getRecent(GlobalConstants.INDEX_PAGE_BLOG_POST_MAX_COUNT, locale);
        List<BlogPostGroupModel> blogPostsGroups = blogPostsService.getRecentWitGroups(blogPostsItems);

        StatisticGeneralModel generalStatistic = statisticService.getGeneralStatistic();

        model.addAttribute("featured", featured);
        model.addAttribute("mostViewedCategories", mostViewedCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("categoriesGroups", categoryGroups);
        model.addAttribute("blogPostsGroups", blogPostsGroups);
        model.addAttribute("generalStatistic", generalStatistic);

        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/" + systemLocale.getLanguage() + "/";
    }

}
