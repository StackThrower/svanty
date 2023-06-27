package com.svanty.scheduler;

import com.svanty.module.stock.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ImageDescriptionGeneratorTask {

    ImagesService imagesService;

    @Autowired
    ImageDescriptionGeneratorTask(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @Scheduled(fixedDelay = 60, initialDelay = 60, timeUnit = TimeUnit.MINUTES)
    public void generateDescriptions () {

//        imagesService.generateDescriptions();
    }
}
