package com.svanty.library.generator;


import com.svanty.constans.Language;
import com.svanty.library.translate.Translator;
import com.svanty.module.stock.db.TagsTranslationsRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.db.entity.TagsTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ImageDescriptionGenerator {

    private static final int FEATURED_TEMPLATE_MAX_VERSIONS = 5;
    private static final int FEATURED_DATE_TEMPLATE_MAX_VERSIONS = 5;
    private static final int FEATURED_DATE_FORMAT_MAX_VERSIONS = 5;
    private static final int TITLE_MAX_VERSIONS = 5;
    private static final int CATEGORY_MAX_VERSIONS = 5;
    private static final int COLOR_MAX_VERSIONS = 5;

    private static final int PARAM_COUNT_IN_DESCRIPTION = 5;
    private static final int TOTAL_PARAM_COUNT = 7;

    public static final int MAX_DESCRIPTION_SYMBOL_COUNT = 145;


    @Autowired
    MessageSource messageSource;

    @Autowired
    TagsTranslationsRepository tagsTranslationsRepository;

    Locale locale = Locale.ENGLISH;

    /**
     * Generate description from image params.
     *
     * @param image
     * @return
     */
    public String generate(Images image) {
// TODO - cut when the descriptions is set
//        String description = image.getDescription();
//        if (description.length() > 60)
//            return getCorrectDescriptionLength(description);
//        else
        return generateNewDescription(image);
    }

    /**
     * Generate new description.
     *
     * @param image
     * @return
     */
    public String generateNewDescription(Images image) {
        StringBuilder ret = new StringBuilder();

        Random random = new Random();
        List<Integer> takenParams = new ArrayList<>();
        for (int i = 0; i < PARAM_COUNT_IN_DESCRIPTION; i++) {
            if (i > 0)
                ret.append(" ✓ ");

            int currentParam;
            do {
                currentParam = random.nextInt(TOTAL_PARAM_COUNT);
            } while (takenParams.contains(currentParam));

            takenParams.add(currentParam);
            try {
                switch (currentParam) {
                    case 0:
                        ret.append(generateIsFeatured(image));
                        break;
                    case 1:
                        ret.append(generateIsFeaturedDate(image));
                        break;
                    case 2:
                        ret.append(generateWithTitle(image));
                        break;
                    case 3:
                        ret.append(generateWithCategory(image));
                        break;
                    case 4:
                        ret.append(generateWithColors(image));
                        break;
                    case 5:
                        ret.append(generateCamera(image));
                        break;
                    case 6:
                        ret.append(generateWithTags(image));
                        break;
                }
            } catch (Exception e) {

            }
        }

        return getCorrectDescriptionLength(ret.toString());
    }

    /**
     * Get correct description string length.
     *
     * @param input
     * @return
     */
    public String getCorrectDescriptionLength(String input) {
        List<String> words = Arrays.stream(input.split(" ")).collect(Collectors.toList());
        StringBuilder ret = new StringBuilder();
        for (String word : words) {
            if ((ret.length() + word.length()) >= MAX_DESCRIPTION_SYMBOL_COUNT) {
                ret.append("...");
                break;
            }

            if (ret.length() != 0)
                ret.append(" ");
            ret.append(word);
        }

        return ret.toString();
    }

    /**
     * Generate with param is featured
     *
     * @param image
     * @return
     */
    String generateIsFeatured(Images image) {

        Images.Featured featured = image.getFeatured();

        String featuredTemplate;
        Random rand = new Random();
        int version;
        switch (featured) {
            case yes:
                version = rand.nextInt(FEATURED_TEMPLATE_MAX_VERSIONS) + 1;
                featuredTemplate = messageSource.getMessage("description-template.featured." + version, new Object[0], locale);
                break;
            case no:
            default:
                version = rand.nextInt(FEATURED_TEMPLATE_MAX_VERSIONS) + 1;
                featuredTemplate = messageSource.getMessage("description-template.not-featured." + version, new Object[0], locale);
                break;
        }

        return featuredTemplate;

    }

    /**
     * Generate with param date featured.
     *
     * @param image
     * @return
     */
    String generateIsFeaturedDate(Images image) {

        Images.Featured featured = image.getFeatured();

        String featuredTemplate;
        Random rand = new Random();
        int version;
        switch (featured) {
            case yes:
                version = rand.nextInt(FEATURED_DATE_TEMPLATE_MAX_VERSIONS) + 1;
                Date featuredDate = image.getFeatured_date();
                String template = messageSource.getMessage("description-template.featured-date." + version, new Object[0], locale);

                int featuredDateFormatVersion = rand.nextInt(FEATURED_DATE_FORMAT_MAX_VERSIONS);
                DateFormat df;
                switch (featuredDateFormatVersion) {
                    case 0:
                    default:
                        df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
                        break;
                    case 1:
                        df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                        break;
                    case 2:
                        df = DateFormat.getDateInstance(DateFormat.LONG, locale);
                        break;
                    case 3:
                        df = DateFormat.getDateInstance(DateFormat.FULL, locale);
                        break;
                }

                featuredTemplate = String.format(template, df.format(featuredDate));
                break;
            case no:
            default:
                version = rand.nextInt(FEATURED_DATE_TEMPLATE_MAX_VERSIONS) + 1;

                featuredTemplate = messageSource.getMessage("description-template.not-featured-date." + version, new Object[0], locale);
                break;
        }

        return featuredTemplate;

    }

    /**
     * Generate with title.
     *
     * @param image
     * @return
     */
    String generateWithTitle(Images image) {

        Random random = new Random();
        int version = random.nextInt(TITLE_MAX_VERSIONS) + 1;
        String template = messageSource.getMessage("description-template.title." + version, new Object[0], locale);

        return String.format(template, Translator.translate(image.getTitle(), Language.ENGLISH));
    }

    /**
     * Generate with category.
     *
     * @param image
     * @return
     */
    String generateWithCategory(Images image) {
        Random random = new Random();
        int version = random.nextInt(CATEGORY_MAX_VERSIONS) + 1;

        String template = messageSource.getMessage("description-template.category." + version, new Object[0], locale);

        return String.format(template, image.getCategory().getName());
    }

    /**
     * Generate with colors.
     *
     * @param image
     * @return
     */
    String generateWithColors(Images image) {

        List<String> colors = Arrays.stream(image.getColors().split(",")).collect(Collectors.toList());
        Random random = new Random();
        int version = random.nextInt(COLOR_MAX_VERSIONS) + 1;

        int colorCount = random.nextInt(colors.size());
        StringBuilder colorsStr = new StringBuilder();
        for (int i = 0; i <= colorCount; i++) {
            if (i > 0)
                colorsStr.append(" ,");
            colorsStr.append("#" + colors.get(i));
        }

        String template = messageSource.getMessage("description-template.color." + version, new Object[0], locale);

        return String.format(template, colorsStr);

    }

    /**
     * Generate with camera.
     *
     * @param image
     * @return
     */
    String generateCamera(Images image) {

        Random random = new Random();
        int version = random.nextInt(COLOR_MAX_VERSIONS) + 1;

        String template = messageSource.getMessage("description-template.camera." + version, new Object[0], locale);

        return String.format(template, image.getCamera());
    }

    /**
     * Generate with tags.
     *
     * @param image
     * @return
     */
    String generateWithTags(Images image) {

        Random random = new Random();
        int version = random.nextInt(COLOR_MAX_VERSIONS) + 1;
        List<String> tags = Arrays.stream(image.getTags().split(",")).collect(Collectors.toList());
        Integer imageVersion = image.getVersion();
        TagsTranslations.Lang langDb = TagsTranslations.Lang.valueOf(Language.ENGLISH.languageCode);

        int tagsCount = random.nextInt(tags.size());
        StringBuilder tagsStr = new StringBuilder();
        for (int i = 0; i <= tagsCount; i++) {
            if (i > 0)
                tagsStr.append(", ");
            if (imageVersion.equals(Images.VERSION_INIT))
                tagsStr.append("#" + Translator.translate(tags.get(i).toLowerCase(), Language.ENGLISH));

            if (imageVersion.equals(Images.VERSION_WITH_TAGS)) {
                String tagSlug = tags.get(i);
                TagsTranslations tagsTranslations = tagsTranslationsRepository.getTagBySlug(tags.get(i), langDb);
                tagsStr.append("#" + (tagsTranslations != null ? tagsTranslations.getTitle().toLowerCase() : tagSlug));
            }
        }

        String template = messageSource.getMessage("description-template.tags." + version, new Object[0], locale);

        return String.format(template, tagsStr);


    }


}
