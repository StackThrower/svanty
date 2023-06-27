package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.Categories;
import com.svanty.module.stock.db.entity.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

    @Query( "select ct from Categories ct where mode = :mode")
    Page<Categories> findWithMode(@Param("mode") Categories.Mode mode, Pageable pageable);

    @Query( "select ct from Categories ct where slug = :slug")
    Categories findWithSlug(@Param("slug") String slug);

    @Query("select ct from Categories ct left join CategoryTranslations ctr on ct.id = ctr.categoryId " +
            "where ctr.categoryId is null")
    Page<Categories> getAllCategoriesPageWithoutTranslation(Pageable pageable);

}
