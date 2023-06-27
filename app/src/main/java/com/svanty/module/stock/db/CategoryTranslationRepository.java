package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.CategoryTranslations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslations, Integer> {

    @Query("from CategoryTranslations where category_id = :categoryId and lang = :lang")
    CategoryTranslations findByCategoryAndLang(@Param("categoryId") Integer categoryId,
                                               @Param("lang") CategoryTranslations.Lang lang);
}
