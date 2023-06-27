package com.svanty.module.stock.service;

import com.svanty.constans.Language;
import com.svanty.db.page.Paged;
import com.svanty.db.page.Paging;
import com.svanty.library.generator.ImageDescriptionGenerator;
import com.svanty.library.slugify.Slugify;
import com.svanty.library.translate.Translator;
import com.svanty.module.core.db.entity.User;
import com.svanty.module.stock.db.*;
import com.svanty.module.stock.db.entity.*;
import com.svanty.module.stock.exception.ImageNotFoundException;
import com.svanty.module.stock.model.*;
import com.svanty.security.UserDetailsSecurity;
import com.svanty.service.PaginationService;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.svanty.module.stock.db.entity.Images.VERSION_INIT;

@Service
public class ImagesService extends PaginationService<ImagesRepository, Images> {

    ImagesRepository imagesRepository;
    StockRepository stockRepository;
    CategoriesRepository categoriesRepository;
    ImageTranslationsRepository imageTranslationsRepository;
    CategoryTranslationRepository categoryTranslationRepository;
    TagsRepository tagsRepository;
    TagsTranslationsRepository tagsTranslationsRepository;

    ImageDescriptionGenerator imageDescriptionGenerator;

    DownloadsRepository downloadsRepository;

    private static final Logger log = LoggerFactory.getLogger(ImagesService.class);

    @Autowired
    ImagesService(ImagesRepository imagesRepository, StockRepository stockRepository,
                  CategoriesRepository categoriesRepository,
                  ImageTranslationsRepository imageTranslationsRepository,
                  CategoryTranslationRepository categoryTranslationRepository,
                  TagsRepository tagsRepository,
                  TagsTranslationsRepository tagsTranslationsRepository,
                  ImageDescriptionGenerator imageDescriptionGenerator,
                  DownloadsRepository downloadsRepository) {
        super(imagesRepository);

        this.imagesRepository = imagesRepository;
        this.stockRepository = stockRepository;
        this.categoriesRepository = categoriesRepository;
        this.imageTranslationsRepository = imageTranslationsRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.tagsRepository = tagsRepository;
        this.tagsTranslationsRepository = tagsTranslationsRepository;

        this.imageDescriptionGenerator = imageDescriptionGenerator;
        this.downloadsRepository = downloadsRepository;
    }


    private String resolutionPreview(String resolution, Boolean thumbnail, Boolean medium) {
        String[] resArr = resolution.split("x");
        Double lWidth = Double.valueOf(resArr[0]);
        Double lHeight = Double.valueOf(resArr[1]);

        Double previewWidth;

        if (lWidth > lHeight) {
            previewWidth = (double) (850 / lWidth);
        } else {
            previewWidth = (double) (480 / lWidth);
        }

        if (thumbnail) {
            if (lWidth > lHeight) {
                previewWidth = (double) (280 / lWidth);
            } else {
                previewWidth = (double) (190 / lWidth);
            }
        }

        if (medium) {
            if (lWidth > lHeight) {
                previewWidth = (double) (640 / lWidth);
            } else {
                previewWidth = (double) (480 / lWidth);
            }
        }

        Long newWidth = Math.round(lWidth * previewWidth);
        Long newHeight = Math.round(lHeight * previewWidth);

        return newWidth + "x" + newHeight;
    }

    /**
     * Prepare page of images
     *
     * @param page
     * @param pageNumber
     * @param size
     * @return
     */
    private Paged<ImageItemModel> processImageItemList(Page<Images> page, Integer pageNumber,
                                                       Integer size) {

        List<Images> images = page.getContent();

        List<ImageItemModel> imageItemModels = new ArrayList<>();
        Slugify slg = new Slugify();

        for (Images image : images) {

            Integer height;
            Integer width;
            String resolution;
            String slug;
            Integer downloadCount = 100;
            Integer likeCount = 12;

            List<Stock> stocks = stockRepository.findByImageWithType(image.getId(), Stock.Type.medium);

            resolution = stocks.stream().map(Stock::getResolution).findFirst().orElse("0x0");
            resolution = resolutionPreview(resolution, false, false);
            String[] resArr = resolution.split("x");
            height = Integer.valueOf(resArr[1]);
            width = Integer.valueOf(resArr[0]);

            slug = slg.slugify(image.getTitle());

            Locale locale = LocaleContextHolder.getLocale();
            Language language = Language.getByCode(locale.getLanguage());

            ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);
            ImageTranslations imageTranslations = imageTranslationsRepository.findByImageAndLanguage(image.getId(), langDb);

            String title = imageTranslations != null ? imageTranslations.getTitle() : image.getTitle();
            String description = imageTranslations != null ? imageTranslations.getDescription() : image.getDescription();


            ImageItemModel imageItemModel = new ImageItemModel(image.getId(),
                    image.getThumbnail(),
                    image.getPreview(),
                    title,
                    description,
                    image.getCategory().getId(),
                    image.getUser(),
                    image.getDate(),
                    image.getStatus(),
                    image.getToken_id(),
                    image.getTags(),
                    image.getExtension(),
                    image.getColors(),
                    image.getExif(),
                    image.getCamera(),
                    image.getFeatured(),
                    image.getFeatured_date(),
                    image.getHow_use_image(),
                    image.getAttribution_required(),
                    image.getOriginal_name(),
                    image.getPrice(),
                    image.getItem_for_sale(),
                    image.getVector(),
                    height,
                    width,
                    resolution,
                    slug,
                    downloadCount,
                    likeCount
            );
            imageItemModels.add(imageItemModel);
        }

        Page<ImageItemModel> retPage = new PageImpl<>(imageItemModels);
        return new Paged<>(retPage, Paging.of(page.getTotalPages(), page.getTotalElements(), pageNumber, size));

    }

    public Paged<ImageItemModel> getFeaturedImageItemPage(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findFeatured(Images.Featured.yes, Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }


    public Paged<ImageItemModel> getPremiumImageItemPage(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findPremium(Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }


    public Paged<ImageItemModel> getLatestImageItemPage(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findLatest(Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }


    public Paged<ImageItemModel> getPopularImageItemPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findPopular(Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }


    public Paged<ImageItemModel> getMostCommentedImageItemPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findMostCommented(Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }

    public Paged<ImageItemModel> getMostViewedImageItemPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findMostViewed(Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }

    public Paged<ImageItemModel> findImageItemPage(String query, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findImages(query, Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }

    public ImageDetailModel getDetailsById(Integer id) throws ImageNotFoundException {
        Optional<Images> imageOpt = imagesRepository.findById(id);
        Images image = imageOpt.orElseThrow(ImageNotFoundException::new);

        List<Stock> stocks = stockRepository.findByImage(image.getId());

        String resolution = stocks.stream().filter(e -> e.getType().equals(Stock.Type.medium))
                .map(Stock::getResolution).findFirst().orElse("0x0");

        resolution = resolutionPreview(resolution, false, false);
        String[] resArr = resolution.split("x");
        Integer height = Integer.valueOf(resArr[1]);
        Integer width = Integer.valueOf(resArr[0]);

        List<String> tagsSlugs = Arrays.stream(image.getTags().split(",")).collect(Collectors.toList());
        List<String> colorsList = Arrays.stream(image.getColors().split(",")).collect(Collectors.toList());
        List<ImageDetailModel> similarImages = getSimilarImages(image);

        Stock maxStock = stocks.stream().filter(e -> e.getType().equals(Stock.Type.large))
                .findFirst().orElse(new Stock());

        Long userImageCount = imagesRepository.getUserTotalImages(Images.Status.active, image.getUser().getId());

        Locale systemLocale = LocaleContextHolder.getLocale();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, systemLocale);
        String formattedDate = df.format(image.getDate());

        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());

        ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);
        ImageTranslations imageTranslations = imageTranslationsRepository.findByImageAndLanguage(image.getId(), langDb);

        String title = imageTranslations != null ? imageTranslations.getTitle() : image.getTitle();
        String description = imageTranslations != null ? imageTranslations.getDescription() : image.getDescription();

        List<TagModel> tags;

        if (image.getVersion() > VERSION_INIT) {
            TagsTranslations.Lang langTagDb = TagsTranslations.Lang.valueOf(language.languageCode);
            tags = tagsTranslationsRepository.getTagsBySlugs(tagsSlugs, langTagDb)
                    .stream()
                    .map(e -> new TagModel(e.getTag().getSlug(),
                            e.getTitle(),
                            e.getTag().getPoster(),
                            e.getDescription(),
                            true)
                    )
                    .collect(Collectors.toList());

        } else {
            tags = new ArrayList<>();
        }

        return new ImageDetailModel(
                image.getId(),
                image.getThumbnail(),
                image.getPreview(),
                title,
                description,
                image.getCategory(),
                image.getUser(),
                image.getDate(),
                image.getStatus(),
                image.getToken_id(),
                tags,
                image.getExtension(),
                colorsList,
                image.getExif(),
                image.getCamera(),
                image.getFeatured(),
                image.getFeatured_date(),
                image.getHow_use_image(),
                image.getAttribution_required(),
                image.getOriginal_name(),
                image.getPrice(),
                image.getItem_for_sale(),
                image.getVector(),
                height,
                width,
                resolution,
                "",
                0,
                0,
                0,
                similarImages,
                stocks,
                maxStock.getExtension().toUpperCase(Locale.ROOT),
                maxStock.getResolution(),
                maxStock.getSize(),
                userImageCount,
                formattedDate,
                maxStock.getToken(),
                image.getVersion()
        );
    }

    private Specification<Images> createSimilarSpecification(Images image) {


        Specification<Images> specification = (images, query, builder) -> {

            List<Predicate> predicateList = new ArrayList<>();

            Predicate status = builder.equal(images.get("status"), Images.Status.active);
            Predicate imageId = builder.notEqual(images.get("id"), image.getId());
            Predicate categoryId = builder.equal(images.get("category"), image.getCategory());

            predicateList.add(status);
            predicateList.add(imageId);
            predicateList.add(categoryId);

            List<Predicate> predicateTagsList = new ArrayList<>();
            List<String> tagsStrArr = Arrays.stream(image.getTags().split(",")).collect(Collectors.toList());
            for (String tagStr : tagsStrArr) {
                predicateTagsList.add(builder.like(images.get("tags"), "%" + tagStr + "%"));
            }

            predicateList.add(builder.or(predicateTagsList.toArray(new Predicate[0])));
            Predicate[] predicateArr = predicateList.toArray(new Predicate[0]);

            return builder.and(predicateArr);
        };

        return specification;
    }

    private List<ImageDetailModel> getSimilarImages(Images image) {

        PageRequest request = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));
        Page<Images> images = imagesRepository.findAll(createSimilarSpecification(image), request);

        Slugify slg = new Slugify();
        List<ImageDetailModel> similarImages = images.getContent().stream().map(
                e -> {
                    List<Stock> stocksSimilar = stockRepository.findByImageWithType(e.getId(), Stock.Type.medium);

                    String resolutionSimilar = stocksSimilar.stream().map(Stock::getResolution).findFirst().orElse("0x0");
                    resolutionSimilar = resolutionPreview(resolutionSimilar, false, false);
                    String[] resArrSimilar = resolutionSimilar.split("x");
                    int heightSimilar = Integer.parseInt(resArrSimilar[1]);
                    int widthSimilar = Integer.parseInt(resArrSimilar[0]);

//                    List<String> tagsSimilar = Arrays.stream(e.getTags().split(",")).collect(Collectors.toList());

                    Locale locale = LocaleContextHolder.getLocale();
                    Language language = Language.getByCode(locale.getLanguage());

                    ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);
                    ImageTranslations imageTranslations = imageTranslationsRepository.findByImageAndLanguage(e.getId(), langDb);

                    String title = imageTranslations != null ? imageTranslations.getTitle() : e.getTitle();
                    String description = imageTranslations != null ? imageTranslations.getDescription() : e.getDescription();

                    String slug = slg.slugify(e.getTitle());
                    return new ImageDetailModel(
                            e.getId(),
                            e.getThumbnail(),
                            e.getPreview(),
                            title,
                            description,
                            new Categories(),
                            e.getUser(),
                            e.getDate(),
                            e.getStatus(),
                            e.getToken_id(),
                            new ArrayList<>(),
                            e.getExtension(),
                            new ArrayList<>(),
                            e.getExif(),
                            e.getCamera(),
                            e.getFeatured(),
                            e.getFeatured_date(),
                            e.getHow_use_image(),
                            e.getAttribution_required(),
                            e.getOriginal_name(),
                            e.getPrice(),
                            e.getItem_for_sale(),
                            e.getVector(),
                            heightSimilar,
                            widthSimilar,
                            resolutionSimilar,
                            slug,
                            0,
                            0,
                            0,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            "",
                            "",
                            "",
                            0l,
                            "",
                            "",
                            e.getVersion()
                    );
                }

        ).collect(Collectors.toList());

        return similarImages;
    }

    public StockItemModel getStockById(Integer id, Stock.Type type) {

        Stock stock = stockRepository.findByImageIdAndStockType(id, type)
                .stream().findFirst().orElse(new Stock());

        Images image = imagesRepository.findById(stock.getImagesId()).orElse(new Images());

        Slugify slugify = new Slugify();
        String slug = slugify.slugify(image.getTitle());

        String fileName = (slug.length() < 50 ? slug : slug.substring(50)) + stock.getResolution() + "." + stock.getExtension();

        return new StockItemModel(
                stock.getId(),
                stock.getImagesId(),
                stock.getName(),
                stock.getType(),
                stock.getExtension(),
                stock.getResolution(),
                stock.getSize(),
                stock.getToken(),
                fileName
        );
    }

    /**
     * Get images by category.
     *
     * @param categoryId
     * @param pageNumber
     * @param size
     * @return
     */
    public Paged<ImageItemModel> getCategoryImageItemPage(Integer categoryId, Integer pageNumber, Integer size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findByCategoryId(categoryId, Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }

    /**
     * Get category by slug.
     *
     * @param slug
     * @return
     */
    public Categories findCategoryWithSlug(String slug) {
        Categories category = categoriesRepository.findWithSlug(slug);

        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());
        CategoryTranslations.Lang langDb = CategoryTranslations.Lang.valueOf(language.languageCode);

        CategoryTranslations categoryTranslations = categoryTranslationRepository.findByCategoryAndLang(category.getId(), langDb);
        String name = categoryTranslations != null ? categoryTranslations.getName() : category.getName();

        category.setName(name);

        return category;
    }


    /**
     * @param categoryId
     * @return
     */
    public Integer findCountByCategory(Integer categoryId) {
        Integer count = imagesRepository.getCountByCategory(categoryId);

        return count;
    }

    /**
     * Get categories
     *
     * @param pageNumber
     * @param size
     * @return
     */
    public Paged<CategoryItemModel> getCategories(Integer pageNumber, Integer size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Categories> categories = categoriesRepository.findAll(request);

        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());
        CategoryTranslations.Lang langDb = CategoryTranslations.Lang.valueOf(language.languageCode);

        List<CategoryItemModel> categoryItemModelList = categories.getContent().stream().map(e -> {

            CategoryTranslations categoryTranslations = categoryTranslationRepository.findByCategoryAndLang(e.getId(), langDb);
            String name = categoryTranslations != null ? categoryTranslations.getName() : e.getName();

            return new CategoryItemModel(
                    e.getId(),
                    name,
                    e.getSlug(),
                    e.getThumbnail(),
                    e.getMode(),
                    imagesRepository.getCountByCategory(e.getId())
            );
        }).collect(Collectors.toList());

        Page<CategoryItemModel> retPage = new PageImpl<>(categoryItemModelList);
        return new Paged<>(retPage, Paging.of(categories.getTotalPages(), categories.getTotalElements(), pageNumber, size));
    }


    public Paged<ImageItemModel> getByUserImageItemPage(Integer userId, Integer pageNumber, Integer size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Images> page = imagesRepository.findByUserId(userId, Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);
    }

    /**
     * Translate all images to language
     */
    public void translateAllImagesWithoutTranslation() {

        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "id"));

        // We get all images without translations at all
        Page<Images> images = imagesRepository
                .getAllImagesWithoutTranslation(pageable);

        Arrays.stream(Language.values())
                .filter(e -> e.enabled)
                .forEach(language -> {

                    ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);

                    for (Images image : images) {

                        ImageTranslations imageTranslations = ImageTranslations.builder()
                                .imagesId(image.getId())
                                .title(Translator.translate(image.getTitle(), language))
                                .description(Translator.translate(image.getDescription(), language))
                                .lang(langDb)
                                .build();

                        log.info("Translate image id={} title={}, description={}", image.getId(),
                                imageTranslations.getTitle(),
                                imageTranslations.getDescription());

                        imageTranslationsRepository.save(imageTranslations);
                    }
                });
    }

    /**
     * Translate tags where image version 0. After the version became 1.
     */
    public void translateTagsWithoutTranslations() {

        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "id"));
        Page<Images> images = imagesRepository.getImagesByVersion(VERSION_INIT, pageable);

        Slugify slugify = new Slugify();
        List<Language> langs = Arrays.stream(Language.values())
                .filter(e -> e.enabled).collect(Collectors.toList());

        for (Images image : images) {

            log.info("Image with version zero id={} tags={}", image.getId(),
                    image.getTags());

            List<String> tags = Arrays.stream(image.getTags().split(",")).collect(Collectors.toList());

            short tagsCount = 0;
            StringBuilder newTagsStr = new StringBuilder();
            for (String tagStr : tags) {

                String translatedTitle = Translator.translate(tagStr, Language.ENGLISH);
                String slug = slugify.slugify(translatedTitle);

                if (tagsCount > 0)
                    newTagsStr.append(",");

                newTagsStr.append(slug);

                if (tagsRepository.getCountBySlug(slug) == 0) {
                    Tags tag = Tags.builder().slug(slug).poster("").approved(Tags.Approved.yes).build();

                    tag = tagsRepository.save(tag);
                    Date currentDate = new Date();

                    log.info("Added tag id={} slug={}", tag.getId(),
                            tag.getSlug());

                    for (Language lang : langs) {

                        TagsTranslations.Lang langDb = TagsTranslations.Lang.valueOf(lang.languageCode);

                        TagsTranslations tagsTranslations = TagsTranslations
                                .builder()
                                .title(Translator.translate(tagStr, lang))
                                .mainText("")
                                .preText("")
                                .description("")
                                .tag(tag)
                                .created(currentDate)
                                .modified(currentDate)
                                .lang(langDb)
                                .build();

                        tagsTranslationsRepository.save(tagsTranslations);

                        log.info("Added tagsTranslation id={} title={}", tagsTranslations.getId(),
                                tagsTranslations.getTitle());
                    }
                }
                tagsCount++;
            }

            image.setTags(newTagsStr.toString());
            image.setVersion(Images.VERSION_WITH_TAGS);
            image.setFeatured_date(new Date());

            image = imagesRepository.save(image);

            log.info("Updated image tags id={} tags={}", image.getId(),
                    image.getTags());

        }
    }

    /**
     * Get tag by slug
     *
     * @param inputText
     * @return
     */
    public TagModel getTagBySlug(String inputText) {
        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());

        TagsTranslations.Lang langDb = TagsTranslations.Lang.valueOf(language.languageCode);
        TagsTranslations tagsTranslations;

        tagsTranslations = tagsTranslationsRepository.getTagBySlug(inputText, langDb);

        if (tagsTranslations != null) {
            return new TagModel(
                    tagsTranslations.getTag().getSlug(),
                    tagsTranslations.getTitle(),
                    tagsTranslations.getTag().getPoster(),
                    tagsTranslations.getDescription(),
                    true);
        } else {

            return new TagModel(inputText, inputText, "", "", false);
        }
    }

    /**
     * Get tag by title
     *
     * @param inputText
     * @return
     */
    public TagModel getTagByTitle(String inputText) {

        TagsTranslations tagsTranslations = tagsTranslationsRepository.getTagByTranslatedTitle(inputText)
                .stream().findFirst().orElse(null);

        if (tagsTranslations != null) {
            return new TagModel(
                    tagsTranslations.getTag().getSlug(),
                    tagsTranslations.getTitle(),
                    tagsTranslations.getTag().getPoster(),
                    tagsTranslations.getDescription(),
                    true);
        }

        return new TagModel(inputText, inputText, "", "", false);
    }


    /**
     * Get images by tag.
     *
     * @param slug
     * @param pageNumber
     * @param size
     * @return
     */
    public Paged<ImageItemModel> getByTagImageItemPage(String slug,
                                                       Integer pageNumber, Integer size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Images> page = imagesRepository.findByTag(slug, Images.Status.active, request);

        return processImageItemList(page, pageNumber, size);

    }

    /**
     * Generate and update empty descriptions.
     */
    public void generateDescriptions() {

        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "id"));
        Page<Images> images = imagesRepository.getImagesWithInvalidDescription(pageable);

        for (Images image : images) {

            String description = imageDescriptionGenerator.generate(image);

            image.setDescription(description);
            image.setModified(new Date());

            imagesRepository.save(image);

            Arrays.stream(Language.values())
                    .filter(e -> e.enabled)
                    .forEach(language -> {

                        ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);

                        ImageTranslations imageTranslations = imageTranslationsRepository.findByImageAndLanguage(image.getId(), langDb);

                        // New version for each language.
                        String descriptionLang =
                                imageDescriptionGenerator.generateNewDescription(image);

                        if (imageTranslations != null)
                            imageTranslations.setDescription(imageDescriptionGenerator.getCorrectDescriptionLength(Translator.translate(descriptionLang, language)));
                        else
                            imageTranslations = ImageTranslations.builder()
                                    .imagesId(image.getId())
                                    .title(Translator.translate(image.getTitle(), language))
                                    .description(imageDescriptionGenerator.getCorrectDescriptionLength(Translator.translate(descriptionLang, language)))
                                    .lang(langDb).build();

                        log.info("Translate image description id={} description={}", image.getId(),
                                imageTranslations.getDescription());

                        imageTranslationsRepository.save(imageTranslations);
                    });
        }
    }

    /**
     * Get page of tags.
     *
     * @param pageNumber
     * @return
     */
    public Paged<TagModel> getTags(Integer pageNumber, Integer size) {

        List<TagModel> tagsItemModel = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "title"));
        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());
        TagsTranslations.Lang lang = TagsTranslations.Lang.valueOf(language.languageCode);

        Page<TagsTranslations> tags = tagsTranslationsRepository.getTags(Tags.Approved.yes, lang, pageable);

        for (TagsTranslations tag : tags) {
            tagsItemModel.add(new TagModel(
                    tag.getTag().getSlug(),
                    tag.getTitle().toLowerCase(),
                    tag.getTag().getPoster(),
                    tag.getDescription(),
                    true
            ));
        }

        Page<TagModel> retPage = new PageImpl<>(tagsItemModel);
        return new Paged<>(retPage, Paging.of(tags.getTotalPages(), tags.getTotalElements(), pageNumber, size));

    }


    public List<ImageItemModel> getRandomImageItemPage(Integer size) {

        Pageable pageable = PageRequest.of(0, size);

        List<Images> images = imagesRepository.findRandom(Images.Status.active, pageable);

        List<ImageItemModel> imageItemModels = new ArrayList<>();
        Slugify slg = new Slugify();

        for (Images image : images) {

            Integer height;
            Integer width;
            String resolution;
            String slug;
            Integer downloadCount = 100;
            Integer likeCount = 12;

            List<Stock> stocks = stockRepository.findByImageWithType(image.getId(), Stock.Type.medium);

            resolution = stocks.stream().map(Stock::getResolution).findFirst().orElse("0x0");
            resolution = resolutionPreview(resolution, false, false);
            String[] resArr = resolution.split("x");
            height = Integer.valueOf(resArr[1]);
            width = Integer.valueOf(resArr[0]);

            slug = slg.slugify(image.getTitle());

            Locale locale = LocaleContextHolder.getLocale();
            Language language = Language.getByCode(locale.getLanguage());

            ImageTranslations.Lang langDb = ImageTranslations.Lang.valueOf(language.languageCode);
            ImageTranslations imageTranslations = imageTranslationsRepository.findByImageAndLanguage(image.getId(), langDb);

            String title = imageTranslations != null ? imageTranslations.getTitle() : image.getTitle();
            String description = imageTranslations != null ? imageTranslations.getDescription() : image.getDescription();

            User user = new User();
            User userDb = image.getUser();
            user.setId(userDb.getId());
            user.setAvatar("https://svanty.com/public/uploads/avatar/" + userDb.getAvatar());
            user.setUsername(userDb.getUsername());

            ImageItemModel imageItemModel = new ImageItemModel(image.getId(),
                    "https://svanty.com/public/uploads/thumbnail/" + image.getThumbnail(),
                    "https://svanty.com/public/uploads/preview/" + image.getPreview(),
                    title,
                    description,
                    image.getCategory().getId(),
                    user,
                    image.getDate(),
                    image.getStatus(),
                    image.getToken_id(),
                    image.getTags(),
                    image.getExtension(),
                    image.getColors(),
                    image.getExif(),
                    image.getCamera(),
                    image.getFeatured(),
                    image.getFeatured_date(),
                    image.getHow_use_image(),
                    image.getAttribution_required(),
                    image.getOriginal_name(),
                    image.getPrice(),
                    image.getItem_for_sale(),
                    image.getVector(),
                    height,
                    width,
                    resolution,
                    slug,
                    downloadCount,
                    likeCount
            );
            imageItemModels.add(imageItemModel);
        }


        return imageItemModels;

    }

    @Transactional
    public String uploadPost(String uploadRootPath, MultipartFile file,
                           String title, String description, Integer categoryId,
                           String tags, String howToUse, String attributionRequired) {
        final Path rootLocation = Paths.get(uploadRootPath);

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
            }

            if(!FileService.isImage(file.getOriginalFilename()))
                throw new RuntimeException("Failed to store file " + file.getOriginalFilename());

            Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }

        String newFileNameLarge = FileService.generateRandomFileName(file.getOriginalFilename(), 90);
        String newFileNamePreview = FileService.generateRandomFileName(file.getOriginalFilename(), 90);
        String newFileNameMedium = FileService.generateRandomFileName(file.getOriginalFilename(), 90);
        String newFileNameSmall =  FileService.generateRandomFileName(file.getOriginalFilename(), 90);
        String newFileNameThumbnail =  FileService.generateRandomFileName(file.getOriginalFilename(), 90);

        String newFileNameLargePath = rootLocation + "/large/" + newFileNameLarge;
        String newFileNamePreviewPath = rootLocation + "/preview/" + newFileNamePreview;
        String newFileNameMediumPath = rootLocation + "/medium/" + newFileNameMedium;
        String newFileNameSmallPath = rootLocation + "/small/" + newFileNameSmall;
        String newFileNameThumbnailPath = rootLocation + "/thumbnail/" + newFileNameThumbnail;

        String path = rootLocation.resolve(file.getOriginalFilename()).toString();
        try {
            File myObj = new File(path);

            FileService.resizeImage(path, newFileNameLargePath, 2500, 250);
            FileService.resizeImage(path, newFileNameMediumPath, 1280, 250);
            FileService.resizeImage(path, newFileNamePreviewPath, 850, 567);
            FileService.resizeImage(path, newFileNameSmallPath, 640, 567);
            FileService.resizeImage(path, newFileNameThumbnailPath, 280, 567);

            myObj.delete();

            String fileExtension = FileService.getFileExtension(file.getOriginalFilename());

            Images image = new Images();

            image.setThumbnail(newFileNameThumbnail);
            image.setPreview(newFileNamePreview);
            image.setTitle(title);
            image.setDescription(description);

            Categories categories = new Categories();
            categories.setId(categoryId);
            image.setCategory(categories);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetailsSecurity userDetailsSecurity = (UserDetailsSecurity) authentication.getPrincipal();
            User user = userDetailsSecurity.getUserDetails();
            image.setUser(user);

            image.setDate(new Date());
            image.setStatus(Images.Status.active);
            image.setToken_id(newFileNamePreview.replace(".", ""));
            image.setTags(tags);
            image.setExtension(fileExtension);
            image.setColors("");
            image.setExif("");
            image.setCamera("");
            image.setFeatured(Images.Featured.no);
            image.setFeatured_date(new Date());
            image.setHow_use_image(Images.HowUseImage.valueOf(howToUse));
            image.setAttribution_required(Images.AttributionRequired.valueOf(attributionRequired));
            image.setOriginal_name(file.getOriginalFilename());
            image.setPrice(new BigDecimal("0.0"));
            image.setItem_for_sale(Images.ItemForSale.free);
            image.setVector("");
            image.setVersion(VERSION_INIT);
            image.setModified(new Date());

            image = imagesRepository.save(image);


            Stock stockLarge = new Stock();
            stockLarge.setImagesId(image.getId());
            stockLarge.setName(newFileNameLarge);
            stockLarge.setType(Stock.Type.large);
            stockLarge.setExtension(fileExtension);
            stockLarge.setResolution(getImageDim(newFileNameLargePath));
            stockLarge.setSize("1M");
            stockLarge.setToken(newFileNameLarge.replace(".", ""));
            stockRepository.save(stockLarge);

            Stock stockMedium = new Stock();
            stockMedium.setImagesId(image.getId());
            stockMedium.setName(newFileNameMedium);
            stockMedium.setType(Stock.Type.medium);
            stockMedium.setExtension(fileExtension);
            stockMedium.setResolution(getImageDim(newFileNameMediumPath));
            stockMedium.setSize("139.5kB");
            stockMedium.setToken(newFileNameMedium.replace(".", ""));
            stockRepository.save(stockMedium);

            Stock stockSmall = new Stock();
            stockSmall.setImagesId(image.getId());
            stockSmall.setName(newFileNameSmall);
            stockSmall.setType(Stock.Type.small);
            stockSmall.setExtension(fileExtension);
            stockSmall.setResolution(getImageDim(newFileNameSmallPath));
            stockSmall.setSize("118.9kB");
            stockSmall.setToken(newFileNameSmall.replace(".", ""));
            stockRepository.save(stockSmall);


            Slugify slg = new Slugify();
            String slug = slg.slugify(image.getTitle());

            Locale locale = LocaleContextHolder.getLocale();
            Language language = Language.getByCode(locale.getLanguage());


            return "/" + language.languageCode + "/photo/" + image.getId() + "/" + slug;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    public void addDownload(Integer imagesId, String ip, Stock.Type type) {

        Downloads downloads = new Downloads();

        downloads.setImagesId(imagesId);
        downloads.setIp(ip != null ? ip : "");
        downloads.setSize(type.toString());
        downloads.setDate(new Date());
        downloads.setUser_id(0);
        downloads.setType("free");

        downloadsRepository.save(downloads);
    }

    private String getImageDim(final String path) {

        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(path));
        } catch (IOException e) {

        }
        int width          = bimg != null ? bimg.getWidth() : 0;
        int height         = bimg != null ? bimg.getHeight() : 0;

        return width + "x" + height;
    }


}
