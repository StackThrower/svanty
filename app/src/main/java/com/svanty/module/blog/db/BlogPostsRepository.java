package com.svanty.module.blog.db;

import com.svanty.module.blog.db.entity.BlogPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostsRepository extends JpaRepository<BlogPosts, Integer> {

    @Query("select bp from BlogPosts bp WHERE post_status = :postStatus and lang = :lang and post_type='post'")
    Page<BlogPosts> getPublishedWithLanguage(@Param("postStatus") String postStatus,
                                             @Param("lang") BlogPosts.Lang lang,
                                             Pageable pageable);

    @Query("select bp from BlogPosts bp where slug = :slug and lang = :lang")
    BlogPosts getBySlug(@Param("slug") String slug, @Param("lang") BlogPosts.Lang lang);

    @Query("select bp from BlogPosts bp WHERE post_status = :postStatus and post_type='post'")
    List<BlogPosts> getPublished(@Param("postStatus") String postStatus);


}
