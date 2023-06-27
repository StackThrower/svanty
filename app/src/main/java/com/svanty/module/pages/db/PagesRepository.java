package com.svanty.module.pages.db;

import com.svanty.module.pages.db.entity.Pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PagesRepository extends JpaRepository<Pages, Integer> {


    @Query("select pg from Pages pg where slug = :slug")
    Pages findBySlug(@Param("slug") String slug);
}
