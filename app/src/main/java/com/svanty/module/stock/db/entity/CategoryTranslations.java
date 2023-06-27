package com.svanty.module.stock.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="category_translations", schema = "public")
public class CategoryTranslations implements Serializable {

    public enum Lang {
        ar, bn, de, en, es, fa, fr, gu, hi,
        it, iw, ja, jv, ko, mr, ms, pt, ru,
        ta, te, tr, uk, ur, vi, zh
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", nullable = false)
    private Lang lang;
}
