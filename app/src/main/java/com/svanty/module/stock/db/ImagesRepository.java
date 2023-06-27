package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.ImageTranslations;
import com.svanty.module.stock.db.entity.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImagesRepository extends JpaRepository<Images, Integer>, JpaSpecificationExecutor<Images> {

    @Query("select tb from Images tb WHERE featured = :featured AND status = :status")
    Page<Images> findFeatured(@Param("featured") Images.Featured featured,
                              @Param("status") Images.Status status,
                              Pageable pageable);

    @Query("select tb from Images tb WHERE price > 0 AND status = :status")
    Page<Images> findPremium(@Param("status") Images.Status status,
                             Pageable pageable);

    @Query("select tb from Images tb WHERE status = :status")
    Page<Images> findLatest(@Param("status") Images.Status status,
                            Pageable pageable);

    @Query("select tb from Images tb WHERE status = :status")
    Page<Images> findPopular(@Param("status") Images.Status status,
                             Pageable pageable);

    @Query("select tb from Images tb WHERE status = :status")
    Page<Images> findMostCommented(@Param("status") Images.Status status,
                                   Pageable pageable);

    @Query("select tb from Images tb WHERE status = :status")
    Page<Images> findMostViewed(@Param("status") Images.Status status,
                                Pageable pageable);

    @Query("select distinct tb from Images tb join ImageTranslations it " +
            "on tb.id = it.imagesId where it.title like %:query% and tb.status = :status")
    Page<Images> findImages(@Param("query") String query, @Param("status") Images.Status status,
                                Pageable pageable);
    @Query("select count(ct) from Images ct where category.id = :categoriesId")
    Integer getCountByCategory(@Param("categoriesId") Integer categoriesId);

    @Query("select count(tb) from Images tb WHERE status = :status")
    Long getCountWithStatus(@Param("status") Images.Status status);

    @Query("select count(tb) from Images tb WHERE status = :status and user_id = :userId")
    Long getUserTotalImages(@Param("status") Images.Status status, @Param("userId") Integer userId);

    @Query("select tb from Images tb where status = :status and category.id = :categoryId")
    Page<Images> findByCategoryId(@Param("categoryId") Integer categoryId,
                                  @Param("status") Images.Status status, Pageable pageable);

    @Query("select count(tb) from Images tb WHERE status = :status and user_id = :userId")
    Long getCountWithStatusAndUserId(@Param("status") Images.Status status, @Param("userId") Integer userId);


    @Query("select tb from Images tb where user_id = :userId and status = :status")
    Page<Images> findByUserId(@Param("userId") Integer userId, @Param("status") Images.Status status, Pageable pageable);

    /**
     * When we need to add new language from configuration
     *
     * @param lang
     * @param pageable
     * @return
     */
    @Query("select tb from Images tb left join ImageTranslations it " +
            "on tb.id = it.imagesId where it.imagesId is null and it.lang = :lang")
    Page<Images> getAllImagesWithoutTranslationWithLang(@Param("lang") ImageTranslations.Lang lang, Pageable pageable);

    /**
     * When the image has no translations at all
     *
     * @param pageable
     * @return
     */
    @Query("select tb from Images tb left join ImageTranslations it " +
            "on tb.id = it.imagesId where it.imagesId is null")
    Page<Images> getAllImagesWithoutTranslation(Pageable pageable);

    /**
     * Get images by version as page.
     *
     * @param version
     * @param pageable
     * @return
     */
    @Query("select tb from Images tb where version = :version")
    Page<Images> getImagesByVersion(@Param("version") Integer version, Pageable pageable);

    /**
     * Get images by tag.
     * @param slug
     * @param active
     * @param request
     * @return
     */
    @Query("select tb from Images tb where tags like %:slug% and status = :status and version > 0")
    Page<Images> findByTag(@Param("slug") String slug,
                           @Param("status") Images.Status active,
                           Pageable request);

    /**
     * Get images with description less than 50 and more 160 symbols.
     * @param pageable
     * @return
     */
    @Query("select tb from Images tb where CHAR_LENGTH(description) < 50 or CHAR_LENGTH(description) > 160")
    Page<Images> getImagesWithInvalidDescription(PageRequest pageable);

    /**
     * Find random images
     * @param status
     * @return
     */
    @Query("select tb from Images tb where status = :status order by rand() ")
    List<Images> findRandom(@Param("status") Images.Status status, Pageable pageable);
}
