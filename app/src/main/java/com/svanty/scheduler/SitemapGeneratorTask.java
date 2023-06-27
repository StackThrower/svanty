package com.svanty.scheduler;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.svanty.constans.Language;
import com.svanty.library.slugify.Slugify;
import com.svanty.module.blog.db.entity.BlogPosts;
import com.svanty.module.blog.service.BlogPostsService;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.db.entity.TagsTranslations;
import com.svanty.module.stock.service.ImagesService;
import com.svanty.module.stock.service.TagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SitemapGeneratorTask {

    private static final Logger log = LoggerFactory.getLogger(SitemapGeneratorTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    ImagesService imagesService;
    BlogPostsService blogPostsService;
    TagsService tagsService;

    @Autowired
    SitemapGeneratorTask(ImagesService imagesService,
                         BlogPostsService blogPostsService,
                         TagsService tagsService) {
        this.imagesService = imagesService;
        this.blogPostsService = blogPostsService;
        this.tagsService = tagsService;
    }


    @Value("${app.sitemap.path}")
    String siteMapDir;

    @Scheduled(fixedDelay = 24, initialDelay = 1, timeUnit = TimeUnit.HOURS)
    public void sitemap() {

        File siteMapIndexFile = new File(siteMapDir + "/index.xml");

        buildImagesSitemap();
        buildBlogSitemap();
        buildTagsSitemap();
        buildPagesSitemap();

        try {
            SitemapIndexGenerator sig = new SitemapIndexGenerator("https://svanty.com/", siteMapIndexFile);
            sig.addUrl("https://svanty.com/public/sitemaps/blog.xml");
            sig.addUrl("https://svanty.com/public/sitemaps/images.xml");
            sig.addUrl("https://svanty.com/public/sitemaps/tags.xml");
            sig.addUrl("https://svanty.com/public/sitemaps/pages.xml");
            sig.write();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Build images site map
     */
    private void buildImagesSitemap() {
        File dir = new File(siteMapDir);
        WebSitemapGenerator wsg;
        try {
            wsg = WebSitemapGenerator.builder("https://svanty.com", dir)
                    .gzip(false)
                    .fileNamePrefix("images")
                    .build();

            List<Images> images = imagesService.findAll(Sort.by(Sort.Direction.DESC, "modified"));
            Slugify slugify = new Slugify();

            List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()))
                    .stream().filter(e -> e.enabled).collect(Collectors.toList());

            for (Images image : images) {

                for (Language language : languages) {

                    WebSitemapUrl url = new WebSitemapUrl
                            .Options("https://svanty.com/" + language.languageCode + "/photo/" +
                            image.getId() +
                            "/" + slugify.slugify(image.getTitle()))
                            .lastMod(image.getModified())
                            .priority(0.7)
                            .changeFreq(ChangeFreq.DAILY)
                            .build();
                    wsg.addUrl(url);
                }
            }

            wsg.write();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Build blog sitemap
     */
    private void buildBlogSitemap() {
        File dir = new File(siteMapDir);
        WebSitemapGenerator wsg;
        try {
            wsg = WebSitemapGenerator.builder("https://svanty.com", dir)
                    .gzip(false)
                    .fileNamePrefix("blog")
                    .build();

            List<BlogPosts> blogPosts = blogPostsService.getPublished();

            for (BlogPosts post : blogPosts) {

                String slug = post.getSlug();

                WebSitemapUrl url = new WebSitemapUrl
                        .Options("https://svanty.com/" + post.getLang() + "/post/" + slug)
                        .lastMod(post.getPostDate())
                        .priority(0.7)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(url);
            }

            wsg.write();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Build tags sitemap.
     */
    private void buildTagsSitemap() {
        File dir = new File(siteMapDir);
        WebSitemapGenerator wsg;
        try {
            wsg = WebSitemapGenerator.builder("https://svanty.com", dir)
                    .gzip(false)
                    .fileNamePrefix("tags")
                    .build();

            List<TagsTranslations> tags = tagsService.getApprovedTags(Sort.by(Sort.Direction.DESC, "modified"));

            for (TagsTranslations tag : tags) {

                String slug = tag.getTag().getSlug();

                WebSitemapUrl url = new WebSitemapUrl
                        .Options("https://svanty.com/" + tag.getLang() + "/tag/" + slug)
                        .lastMod(tag.getModified())
                        .priority(0.7)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(url);
            }

            wsg.write();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Build pages sitemap
     */
    private void buildPagesSitemap() {
        File dir = new File(siteMapDir);
        WebSitemapGenerator wsg;
        try {
            wsg = WebSitemapGenerator.builder("https://svanty.com", dir)
                    .gzip(false)
                    .fileNamePrefix("pages")
                    .build();

            Arrays.stream(Language.values()).filter(e -> e.enabled).forEach(e -> {

                        WebSitemapUrl url = null;
                        try {
                            url = new WebSitemapUrl
                                    .Options("https://svanty.com/" + e.languageCode)
                                    .priority(0.7)
                                    .changeFreq(ChangeFreq.DAILY)
                                    .build();
                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        }
                        wsg.addUrl(url);

                    }
            );


            wsg.write();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


}




