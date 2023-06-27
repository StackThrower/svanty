package com.svanty.module.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BlogPostGroupModel {

    private String group;
    private List<BlogPostItemModel> items;
}
