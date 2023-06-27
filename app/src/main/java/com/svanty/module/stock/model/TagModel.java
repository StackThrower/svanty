package com.svanty.module.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagModel {

    private String slug;
    private String title;
    private String poster;
    private String description;
    private boolean found;
}
