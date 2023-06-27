package com.svanty.module.stock.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "downloads", schema = "public")
public class Downloads implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="images_id", nullable = false)
    private Integer imagesId;

    @Column(name="user_id", nullable = false)
    private Integer user_id;

    @Column(name="ip", nullable = false, length = 25)
    private String ip;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name="type", nullable = false, length = 5)
    private String type;

    @Column(name="size", nullable = false, length = 10)
    private String size;
}
