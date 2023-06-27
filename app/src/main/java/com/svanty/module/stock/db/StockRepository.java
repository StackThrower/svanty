package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query( "from Stock where images_id = :imagesId AND type = :type")
    List<Stock> findByImageWithType(@Param("imagesId") Integer imagesId, @Param("type") Stock.Type type);

    @Query( "from Stock where images_id = :imagesId")
    List<Stock> findByImage(@Param("imagesId") Integer imagesId);

    @Query("from Stock st where st.imagesId = :id and st.type = :type")
    List<Stock> findByImageIdAndStockType(@Param("id") Integer id, @Param("type") Stock.Type type);


}
