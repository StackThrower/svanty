package com.svanty.module.stock.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "tags_translations", schema = "public")
public class TagsTranslations {

    public enum Lang {
        ar, bn, de, en, es, fa, fr, gu, hi,
        it, iw, ja, jv, ko, mr, ms, pt, ru,
        ta, te, tr, uk, ur, vi, zh
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 512)
    private String title;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name="pre_text", nullable = false, columnDefinition = "TEXT")
    private String preText;

    @Column(name="main_text", nullable = false, columnDefinition = "TEXT")
    private String mainText;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Enumerated(EnumType.STRING)
    @Column(name="lang", nullable = false)
    private Lang lang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tags tag;
}
