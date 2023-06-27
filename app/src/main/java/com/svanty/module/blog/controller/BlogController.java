package com.svanty.module.blog.controller;

import com.svanty.constans.GlobalConstants;
import com.svanty.module.blog.model.BlogPostItemModel;
import com.svanty.module.blog.service.BlogPostsService;
import com.svanty.module.core.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BlogController {

    private BlogPostsService blogPostsService;

    @Autowired
    BlogController(BlogPostsService blogPostsService) {
        this.blogPostsService = blogPostsService;
    }

    /**
     * Main page of blog
     * @param locale
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/{locale}/blog")
    public String index(
            @PathVariable String locale,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
            Model model) {

        List<BlogPostItemModel> items = blogPostsService.getRecent(GlobalConstants.INDEX_PAGE_BLOG_POST_MAX_COUNT, locale);

        model.addAttribute("mainItem", items.size() > 0 ? items.get(0) : new BlogPostItemModel());
        model.addAttribute("blogItems", items.size() > 1 ? items.subList(1, 4) : new ArrayList<>());

        return "module/blog/records";
    }

    /**
     * Get post by url with locale
     * @param slug
     * @param model
     * @return
     */
    @GetMapping("/{locale}/post/{slug}")
    public String post(
            @PathVariable String locale,
            @PathVariable String slug, Model model) {

        BlogPostItemModel post = blogPostsService.getBySlug(slug, locale);

        model.addAttribute("post", post);

        return "module/blog/post";
    }



    @GetMapping("/{locale}/blog/tag/{tag}")
    public String tag(@PathVariable String tag) {

        return "module/blog/records";
    }

    @GetMapping("/blog/{locale}/{slug}")
    public String legacyRedirect(@PathVariable("locale") String locale, @PathVariable String slug) {

        slug = URLEncoder.encode(slug, StandardCharsets.UTF_8);

        return "redirect:/" + locale + "/post/" + slug;
    }

    @GetMapping("/blog/{slug}")
    public String legacyRedirectEn(@PathVariable String slug) {
        return "redirect:/en/post/" + slug;
    }

}
