package com.svanty.module.stock.db.entity;

import com.svanty.module.core.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "images", schema = "public")
public class Images implements Serializable {

    public static Integer VERSION_INIT = 0;
    public static Integer VERSION_WITH_TAGS = 1;

    public enum Status {
        active, pending
    }

    public enum Featured {
        yes, no
    }

    public enum HowUseImage {
        free,
        free_personal,
        editorial_only,
        web_only
    }

    public enum AttributionRequired {
        yes, no
    }

    public enum ItemForSale {
        free, sale
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="thumbnail", nullable = false, length = 255)
    private String thumbnail;

    @Column(name="preview", nullable = false, length = 100)
    private String preview;

    @Column(name="title", nullable = false, length = 255)
    private String title;

    @Column(name="description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @JoinColumn(name="categories_id")
    private Categories category;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @Column(name="token_id", nullable = false, length = 255)
    private String token_id;

    @Column(name="tags", nullable = false, columnDefinition = "TEXT")
    private String tags;

    @Column(name="extension", nullable = false, length = 25)
    private String extension;

    @Column(name="colors", nullable = false, columnDefinition = "TEXT")
    private String colors;

    @Column(name="exif", nullable = false, length = 255)
    private String exif;

    @Column(name="camera", nullable = false, length = 255)
    private String camera;

    @Enumerated(EnumType.STRING)
    @Column(name="featured", nullable = false)
    private Featured featured;

    @Column(name="featured_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date featured_date;

    @Enumerated(EnumType.STRING)
    @Column(name="how_use_image", nullable = false)
    private HowUseImage how_use_image;

    @Enumerated(EnumType.STRING)
    @Column(name="attribution_required", nullable = false)
    private AttributionRequired attribution_required;

    @Column(name="original_name", nullable = false, length = 255)
    private String original_name;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name="item_for_sale", nullable = false)
    private ItemForSale item_for_sale;

    @Column(name="vector", nullable = false, length = 3)
    private String vector;

    @Column(name = "version", nullable = true)
    private Integer version;

    @Column(name="modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
}
