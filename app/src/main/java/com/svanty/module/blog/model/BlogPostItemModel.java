package com.svanty.module.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostItemModel {

    Integer id;
    Date postDate;
    String title;
    String content;
    String slug;
    String postStatus;
    String description;
    String poster;
}
