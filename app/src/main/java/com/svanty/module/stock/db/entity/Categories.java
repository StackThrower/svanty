package com.svanty.module.stock.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "categories", schema = "public")
public class Categories implements Serializable {

    public enum Mode {
        on, off
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="slug", nullable = false, length = 200)
    private String slug;

    @Column(name="thumbnail", nullable = false, length = 150)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name="mode", nullable = false)
    private Mode mode;
}
