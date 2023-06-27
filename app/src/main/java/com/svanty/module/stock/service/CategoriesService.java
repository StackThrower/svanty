package com.svanty.module.stock.service;

import com.svanty.constans.Language;
import com.svanty.library.translate.Translator;
import com.svanty.module.stock.db.CategoriesRepository;
import com.svanty.module.stock.db.CategoryTranslationRepository;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.entity.Categories;
import com.svanty.module.stock.db.entity.CategoryTranslations;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.model.CategoryGroupModel;
import com.svanty.module.stock.model.CategoryItemModel;
import com.svanty.service.PaginationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriesService extends PaginationService<CategoriesRepository, Images> {

    private static final Logger log = LoggerFactory.getLogger(CategoriesService.class);

    CategoriesRepository categoriesRepository;
    ImagesRepository imagesRepository;
    CategoryTranslationRepository categoryTranslationRepository;

    @Autowired
    CategoriesService(CategoriesRepository categoriesRepository, ImagesRepository imagesRepository,
                      CategoryTranslationRepository categoryTranslationRepository) {
        super(categoriesRepository);

        this.categoriesRepository = categoriesRepository;
        this.imagesRepository = imagesRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;
    }


    public List<CategoryItemModel> getCategoryItemPageListWithCounts(int size) {

        final Integer pageNumber = 0;
        PageRequest request = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Categories> postPage = categoriesRepository.findWithMode(Categories.Mode.on, request);

        List<Categories> categories = postPage.getContent();
        List<CategoryItemModel> categoryItemModelList = new ArrayList<>();

        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());
        CategoryTranslations.Lang langDb = CategoryTranslations.Lang.valueOf(language.languageCode);


        for (Categories category : categories) {
            Integer count = imagesRepository.getCountByCategory(category.getId());

            CategoryTranslations categoryTranslations = categoryTranslationRepository.findByCategoryAndLang(category.getId(), langDb);
            String name = categoryTranslations != null ? categoryTranslations.getName() : category.getName();

            CategoryItemModel categoryItemModel = new CategoryItemModel(
                    category.getId(),
                    name,
                    category.getSlug(),
                    category.getThumbnail(),
                    category.getMode(),
                    count
            );
            categoryItemModelList.add(categoryItemModel);
        }

        return categoryItemModelList;
    }


    public List<CategoryItemModel> getCategoryItemPageList(int size) {

        final Integer pageNumber = 0;
        PageRequest request = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Categories> postPage = categoriesRepository.findWithMode(Categories.Mode.on, request);

        List<Categories> categories = postPage.getContent();
        List<CategoryItemModel> categoryItemModelList = new ArrayList<>();

        Locale locale = LocaleContextHolder.getLocale();
        Language language = Language.getByCode(locale.getLanguage());
        CategoryTranslations.Lang langDb = CategoryTranslations.Lang.valueOf(language.languageCode);

        for (Categories category : categories) {
            Integer count = 0;

            CategoryTranslations categoryTranslations = categoryTranslationRepository.findByCategoryAndLang(category.getId(), langDb);
            String name = categoryTranslations != null ? categoryTranslations.getName() : category.getName();

            CategoryItemModel categoryItemModel = new CategoryItemModel(
                    category.getId(),
                    name,
                    category.getSlug(),
                    category.getThumbnail(),
                    category.getMode(),
                    count
            );
            categoryItemModelList.add(categoryItemModel);
        }

        return categoryItemModelList;
    }


    public List<CategoryGroupModel> createGroups(List<CategoryItemModel> categories) {
        List<CategoryGroupModel> ret = new ArrayList<>();

        for (int i = 0; i <= categories.size() - 3; i += 3) {

            CategoryGroupModel category =
                    new CategoryGroupModel("group", new ArrayList<>(categories.subList(i, i + 3)));

            ret.add(category);
        }

        return ret;
    }

    /**
     * Get most viewed categories
     *
     * @param categories
     * @param count
     * @return
     */
    public List<CategoryItemModel> getMostViewed(List<CategoryItemModel> categories, Integer count) {

        List<CategoryItemModel> sorted = categories.stream()
                .sorted(Comparator
                        .comparingInt(CategoryItemModel::getCount)
                        .reversed())
                .collect(Collectors.toList());

        return (sorted.size() >= count) ? sorted.subList(0, count) : new ArrayList<>();
    }

    /**
     * Translate all categories with enabled Language flag
     */
    public void translateAllCategoriesWithoutTranslation() {

        PageRequest pageable = PageRequest.of(0, 25, Sort.by(Sort.Direction.DESC, "id"));

        Page<Categories> categories = categoriesRepository.getAllCategoriesPageWithoutTranslation(pageable);

        Arrays.stream(Language.values())
                .filter(e -> e.enabled)
                .forEach(language -> {

                    CategoryTranslations.Lang langDb = CategoryTranslations.Lang.valueOf(language.languageCode);

                    for (Categories category : categories) {

                        CategoryTranslations categoryTranslation = CategoryTranslations
                                .builder()
                                .categoryId(category.getId())
                                .name(Translator.translate(category.getName(), language))
                                .lang(langDb)
                                .build();

                        log.info("Translate category id={}, name={}", categoryTranslation.getCategoryId(),
                                categoryTranslation.getName());

                        categoryTranslationRepository.save(categoryTranslation);
                    }
                });

    }

}
