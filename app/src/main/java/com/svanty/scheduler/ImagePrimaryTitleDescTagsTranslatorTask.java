package com.svanty.scheduler;

import com.svanty.module.stock.service.ImagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
public class ImagePrimaryTitleDescTagsTranslatorTask {

    private static final Logger log = LoggerFactory.getLogger(ImagePrimaryTitleDescTagsTranslatorTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    ImagesService imagesService;

    @Autowired
    ImagePrimaryTitleDescTagsTranslatorTask(ImagesService imagesService) {
        this.imagesService = imagesService;
    }


    @Scheduled(fixedDelay = 10, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void TranslateImages() {

        imagesService.translateAllImagesWithoutTranslation();
    }

}
