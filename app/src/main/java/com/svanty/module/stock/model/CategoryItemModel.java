package com.svanty.module.stock.model;

import com.svanty.module.stock.db.entity.Categories;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryItemModel {

    private Integer id;

    private String name;

    private String slug;

    private String thumbnail;

    private Categories.Mode mode;

    private Integer count;
}
