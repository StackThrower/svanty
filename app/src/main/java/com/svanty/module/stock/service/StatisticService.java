package com.svanty.module.stock.service;

import com.svanty.module.core.db.UserRepository;
import com.svanty.module.stock.db.DownloadsRepository;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.model.StatisticGeneralModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    ImagesRepository imagesRepository;
    DownloadsRepository downloadsRepository;
    UserRepository userRepository;

    @Autowired
    StatisticService(ImagesRepository imagesRepository, DownloadsRepository downloadsRepository, UserRepository userRepository) {
        this.imagesRepository = imagesRepository;
        this.downloadsRepository = downloadsRepository;
        this.userRepository = userRepository;
    }

    public StatisticGeneralModel getGeneralStatistic() {

        Long imagesCount = imagesRepository.getCountWithStatus(Images.Status.active);
        Long downloadsCount = downloadsRepository.count();
        Long userCount = userRepository.count();

        return new StatisticGeneralModel(imagesCount, downloadsCount, userCount);
    }


}
