package com.svanty.module.stock.service;

import com.svanty.constans.Language;
import com.svanty.library.generator.TagDescriptionGenerator;
import com.svanty.library.translate.Translator;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.TagsRepository;
import com.svanty.module.stock.db.TagsTranslationsRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.db.entity.Tags;
import com.svanty.module.stock.db.entity.TagsTranslations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TagsService {

    private static final Logger log = LoggerFactory.getLogger(TagsService.class);

    TagsRepository tagsRepository;
    TagsTranslationsRepository tagsTranslationsRepository;
    ImagesRepository imagesRepository;
    TagDescriptionGenerator tagDescriptionGenerator;

    @Autowired
    TagsService(TagsRepository tagsRepository,
                ImagesRepository imagesRepository,
                TagsTranslationsRepository tagsTranslationsRepository,
                TagDescriptionGenerator tagDescriptionGenerator) {
        this.tagsRepository = tagsRepository;
        this.imagesRepository = imagesRepository;
        this.tagsTranslationsRepository = tagsTranslationsRepository;

        this.tagDescriptionGenerator = tagDescriptionGenerator;
    }

    /**
     * Generate metadata for empty tags.
     */
    public void generateMetadata() {

        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "id"));
        PageRequest pageableImages = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<TagsTranslations> tags = tagsTranslationsRepository.getTagsWithoutMetadata(Tags.Approved.yes, pageable);

        for(TagsTranslations tag: tags) {

            Page<Images> images = imagesRepository.findByTag(tag.getTag().getSlug(), Images.Status.active, pageableImages);
            if(images.getContent().size() > 0) {

                String description = tagDescriptionGenerator.generate(images.getContent());

                String poster = images.getContent().stream()
                        .map(Images::getPreview).min((o1, o2) -> ThreadLocalRandom.current()
                                .nextInt(-1, 2)).orElse("");

                tag.setModified(new Date());
                Language language = Language.getByCode(tag.getLang().toString());
                tag.setDescription(tagDescriptionGenerator.getCorrectDescriptionLength(Translator.translate(description, language)));

                tagsTranslationsRepository.save(tag);

                Tags mainTag = tagsRepository.getById(tag.getTag().getId());
                mainTag.setPoster(poster);
                tagsRepository.save(mainTag);

                log.info("tag: tag={} description={} poster={}", tag.getTag().getSlug(),
                        tag.getDescription(), tag.getTag().getPoster());
            } else {
                Tags mainTag = tagsRepository.getById(tag.getTag().getId());
                mainTag.setApproved(Tags.Approved.no);
                tagsRepository.save(mainTag);
            }
        }
    }

    /**
     * Get all approved tags.
     * @return
     */
    public List<TagsTranslations> getApprovedTags(Sort sort) {
        List<TagsTranslations> tags = tagsTranslationsRepository.getTagsWithStatus(Tags.Approved.yes, sort);

        return tags;
    }
}
