package com.svanty.module.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticGeneralModel {

    private Long imagesCount;
    private Long downloadsCount;
    private Long usersCount;
}
