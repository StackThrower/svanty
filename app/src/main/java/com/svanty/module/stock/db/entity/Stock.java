package com.svanty.module.stock.db.entity;

import com.svanty.module.core.db.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "stock", schema = "public")
public class Stock implements Serializable {

    public enum Type {
        small, medium, large, vector
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="images_id", nullable = false)
    private Integer imagesId;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private Type type;

    @Column(name="extension", nullable = false, length = 25)
    private String extension;

    @Column(name="resolution", nullable = false, length = 100)
    private String resolution;

    @Column(name="size", nullable = false, length = 50)
    private String size;

    @Column(name="token", nullable = false, length = 200)
    private String token;
}
