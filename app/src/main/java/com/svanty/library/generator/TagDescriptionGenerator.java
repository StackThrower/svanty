package com.svanty.library.generator;

import com.svanty.constans.Language;
import com.svanty.library.translate.Translator;
import com.svanty.module.stock.db.TagsTranslationsRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.db.entity.TagsTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TagDescriptionGenerator {

    private static final int TITLE_MAX_VERSIONS = 5;
    private static final int CATEGORY_MAX_VERSIONS = 5;
    private static final int COLOR_MAX_VERSIONS = 5;

    private static final int PARAM_COUNT_IN_DESCRIPTION = 2;
    private static final int TOTAL_PARAM_COUNT = 5;

    public static final int MAX_DESCRIPTION_SYMBOL_COUNT = 145;
    private static final int MAX_INVOLVED_IMAGES_IN_DESCRIPTION = 2;


    @Autowired
    MessageSource messageSource;

    @Autowired
    TagsTranslationsRepository tagsTranslationsRepository;

    Locale locale = Locale.ENGLISH;

    /**
     * Generate description from image params.
     *
     * @param images
     * @return
     */
    public String generate(List<Images> images) {
        return generateNewDescription(images);
    }

    /**
     * Generate new description.
     *
     * @param image
     * @return
     */
    public String generateNewDescription(List<Images> images) {
        StringBuilder ret = new StringBuilder();
        Random random = new Random();


        for (int j = 0; j < MAX_INVOLVED_IMAGES_IN_DESCRIPTION; j++) {

            List<Integer> takenParams = new ArrayList<>();
            if (j >= images.size())
                continue;
            if (j > 0)
                ret.append(" ");


            Images image = images.get(j);

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
                            ret.append(generateWithTitle(image));
                            break;
                        case 1:
                            ret.append(generateWithCategory(image));
                            break;
                        case 2:
                            ret.append(generateWithColors(image));
                            break;
                        case 3:
                            ret.append(generateCamera(image));
                            break;
                        case 4:
                            ret.append(generateWithTags(image));
                            break;
                    }
                } catch (Exception e) {

                }
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
