package com.svanty.module.stock.model;

import com.svanty.module.core.db.entity.User;
import com.svanty.module.stock.db.entity.Categories;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.db.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ImageDetailModel {

    private Integer id;

    private String thumbnail;

    private String preview;

    private String title;

    private String description;

    private Categories category;

    private User user;

    private Date date;

    private Images.Status status;

    private String token_id;

    private List<TagModel> tags;

    private String extension;

    private List<String> colors;

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

    private Integer viewCount;

    private List<ImageDetailModel> similarImages;

    private List<Stock> files;

    private String fileType;

    private String maxResolution;

    private String maxFileSize;

    private Long userImageCount;

    private String formattedDate;

    private String token;

    private Integer version;
}
