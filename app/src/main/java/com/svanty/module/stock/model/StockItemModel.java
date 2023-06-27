package com.svanty.module.stock.model;

import com.svanty.module.stock.db.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockItemModel {

    Integer id;

    Integer imagesId;

    String name;

    Stock.Type type;

    String extension;

    String resolution;

    String size;

    String token;

    String fileName;
}
