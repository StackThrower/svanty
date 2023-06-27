package com.svanty.module.blog.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "blog_posts", schema = "public")
public class BlogPosts implements Serializable {

    public final static String STATUS_PUBLISH = "publish";

    public enum Lang {
        ar, bn, de, en, es, fa, fr, gu, hi,
        it, iw, ja, jv, ko, mr, ms, pt, ru,
        ta, te, tr, uk, ur, vi, zh
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;

    @Column(name="post_title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name="post_content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="post_name", nullable = false, length = 200)
    private String slug;

    @Column(name="post_status", nullable = false, length = 20)
    private String postStatus;

    @Column(name="post_description", nullable = false, length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="lang", nullable = false)
    private Lang lang;

    @Column(name = "post_poster", nullable = false, length = 350)
    private String poster;
}
