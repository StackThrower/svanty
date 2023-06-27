package com.svanty.scheduler;

import com.svanty.module.stock.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TagMetadataGeneratorTask {

    TagsService tagsService;

    @Autowired
    TagMetadataGeneratorTask(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @Scheduled(initialDelay = 60, fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void generateMetaData() {
        tagsService.generateMetadata();
    }
}
