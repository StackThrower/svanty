package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.Tags;
import com.svanty.module.stock.db.entity.TagsTranslations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsTranslationsRepository extends JpaRepository<TagsTranslations, Integer> {

    @Query("select tt from TagsTranslations tt join Tags tg " +
            "on tg.id = tt.tag.id where tg.slug in :slugs and tt.lang = :lang")
    List<TagsTranslations> getTagsBySlugs(@Param("slugs") List<String> slugs,
                                          @Param("lang") TagsTranslations.Lang lang);

    /**
     * Get tag translation by slug
     *
     * @param slug
     * @param langDb
     * @return
     */
    @Query("select tt from TagsTranslations tt join Tags tg " +
            "on tg.id=tt.tag.id where tg.slug = :slug and tt.lang = :lang")
    TagsTranslations getTagBySlug(@Param("slug") String slug, @Param("lang") TagsTranslations.Lang langDb);

    /**
     * Get tag translation by title
     *
     * @param title
     * @return
     */
    @Query("select tt from TagsTranslations tt where title = :title")
    List<TagsTranslations> getTagByTranslatedTitle(@Param("title") String title);

    /**
     * Get tags without metadata.
     *
     * @param approved
     * @param pageable
     * @return
     */
    @Query("select tt from TagsTranslations tt " +
            "where tt.tag.approved = :approved and (char_length(tt.description) < 50 or char_length(tt.description) > 160)")
    Page<TagsTranslations> getTagsWithoutMetadata(@Param("approved") Tags.Approved approved, Pageable pageable);


    /**
     * Get all approved tags.
     *
     * @param yes
     * @return
     */
    @Query("select tt from TagsTranslations tt where tt.tag.approved = :approved")
    List<TagsTranslations> getTagsWithStatus(@Param("approved") Tags.Approved yes, Sort sort);

    /**
     * Get tags by page with status.
     *
     * @param approved
     * @param lang
     * @param pageable
     * @return
     */
    @Query("select tt from TagsTranslations tt where tt.tag.approved = :approved and tt.lang = :lang")
    Page<TagsTranslations> getTags(@Param("approved") Tags.Approved approved, @Param("lang") TagsTranslations.Lang lang,
                                   Pageable pageable);
}
