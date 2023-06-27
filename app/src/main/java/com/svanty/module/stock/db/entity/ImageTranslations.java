package com.svanty.module.stock.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="image_translations", schema = "public")
public class ImageTranslations implements Serializable {

    public enum Lang {
        ar, bn, de, en, es, fa, fr, gu, hi,
        it, iw, ja, jv, ko, mr, ms, pt, ru,
        ta, te, tr, uk, ur, vi, zh
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "images_id", nullable = false)
    private Integer imagesId;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", nullable = false)
    private Lang lang;

}
