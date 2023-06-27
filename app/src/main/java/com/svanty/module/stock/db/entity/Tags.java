package com.svanty.module.stock.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tags", schema = "public")
public class Tags {

    public enum Approved {
        yes, no
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slug", nullable = false, length = 512)
    private String slug;

    @Column(name = "poster", nullable = false, length = 512)
    private String poster;

    @Enumerated(EnumType.STRING)
    @Column(name = "approved", nullable = false)
    Approved approved;
}
