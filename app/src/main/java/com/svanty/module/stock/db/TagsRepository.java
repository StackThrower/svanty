package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {

    /**
     * Get count tags by slug.
     * @param slug
     * @return
     */
    @Query("select count(tg) from Tags tg where slug = :slug")
    Integer getCountBySlug(@Param("slug") String slug);
}
