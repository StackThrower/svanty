package com.svanty.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="admin_settings")
public class AdminSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "google_analytics", nullable = false, columnDefinition = "TEXT")
    String googleAnalytics;

    @Column(name = "google_adsense", nullable = false, columnDefinition = "TEXT")
    String googleAdsense;

}
