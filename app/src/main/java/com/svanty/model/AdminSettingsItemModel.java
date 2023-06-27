package com.svanty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSettingsItemModel {

    Integer id;
    String googleAnalytics;
    String googleAdsense;
}
