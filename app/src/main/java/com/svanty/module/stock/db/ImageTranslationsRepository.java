package com.svanty.module.stock.db;


import com.svanty.module.stock.db.entity.ImageTranslations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageTranslationsRepository extends JpaRepository<ImageTranslations, Integer> {

    @Query("from ImageTranslations where imagesId = :id and lang = :lang")
    ImageTranslations findByImageAndLanguage(@Param("id") Integer id,
                                             @Param("lang") ImageTranslations.Lang lang);
}
