package com.svanty.module.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryGroupModel {

    private String name;

    private List<CategoryItemModel> items;

}
