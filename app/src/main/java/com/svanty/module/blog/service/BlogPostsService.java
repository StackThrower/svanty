package com.svanty.module.blog.service;

import com.svanty.module.blog.db.BlogPostsRepository;
import com.svanty.module.blog.db.entity.BlogPosts;
import com.svanty.module.blog.model.BlogPostGroupModel;
import com.svanty.module.blog.model.BlogPostItemModel;
import com.svanty.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostsService extends PaginationService<BlogPostsRepository, BlogPosts> {

    BlogPostsRepository blogPostsRepository;

    @Autowired
    BlogPostsService(BlogPostsRepository blogPostsRepository) {
        super(blogPostsRepository);

        this.blogPostsRepository = blogPostsRepository;
    }

    public List<BlogPostItemModel> getRecent(int size, String lang) {

        final Integer pageNumber = 0;
        PageRequest request = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<BlogPosts> blogPostsPage = blogPostsRepository.getPublishedWithLanguage(BlogPosts.STATUS_PUBLISH,
                BlogPosts.Lang.valueOf(lang),
                request);

        List<BlogPosts> blogPosts = blogPostsPage.getContent();

        return blogPosts.stream().map(e -> {

            return new BlogPostItemModel(
                    e.getId(),
                    e.getPostDate(),
                    e.getTitle(),
                    e.getContent(),
                    e.getSlug(),
                    e.getPostStatus(),
                    e.getDescription(),
                    e.getPoster()
            );
        }).collect(Collectors.toList());

    }


    public List<BlogPosts> getPublished() {
        return blogPostsRepository.getPublished(BlogPosts.STATUS_PUBLISH);
    }

    public List<BlogPostGroupModel> getRecentWitGroups(List<BlogPostItemModel> blogPosts) {
        List<BlogPostGroupModel> ret = new ArrayList<>();

        for (int i = 0; i <= blogPosts.size() - 3; i += 3) {

            BlogPostGroupModel group =
                    new BlogPostGroupModel("group", new ArrayList<>(blogPosts.subList(i, i + 3)));

            ret.add(group);
        }

        if (blogPosts.size() < 3 && blogPosts.size() > 0) {
            BlogPostGroupModel group =
                    new BlogPostGroupModel("group", new ArrayList<>(blogPosts));

            ret.add(group);
        }

        return ret;
    }

    /**
     * Get post by slug with language.
     *
     * @param slug
     * @param lang
     * @return
     */
    public BlogPostItemModel getBySlug(String slug, String lang) {

        slug = URLEncoder.encode(slug, StandardCharsets.UTF_8);

        BlogPosts post = blogPostsRepository.getBySlug(slug, BlogPosts.Lang.valueOf(lang));

        return post != null ? new BlogPostItemModel(
                post.getId(),
                post.getPostDate(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.getPostStatus(),
                post.getDescription(),
                post.getPoster()
        ) : new BlogPostItemModel();
    }


}
