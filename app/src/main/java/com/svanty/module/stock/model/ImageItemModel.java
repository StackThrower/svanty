package com.svanty.module.stock.model;

import com.svanty.module.core.db.entity.User;
import com.svanty.module.stock.db.entity.Images;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class ImageItemModel {

    private Integer id;

    private String thumbnail;

    private String preview;

    private String title;

    private String description;

    private Integer categories_id;

    private User user;

    private Date date;

    private Images.Status status;

    private String token_id;

    private String tags;

    private String extension;

    private String colors;

    private String exif;

    private String camera;

    private Images.Featured featured;

    private Date featured_date;

    private Images.HowUseImage how_use_image;

    private Images.AttributionRequired attribution_required;

    private String original_name;

    private BigDecimal price;

    private Images.ItemForSale item_for_sale;

    private String vector;

    private Integer height;

    private Integer width;

    private String resolution;

    private String slug;

    private Integer downloadCount;

    private Integer likeCount;
}
