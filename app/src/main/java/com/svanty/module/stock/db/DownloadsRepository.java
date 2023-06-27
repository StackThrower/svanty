package com.svanty.module.stock.db;

import com.svanty.module.stock.db.entity.Downloads;
import com.svanty.module.stock.db.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadsRepository extends JpaRepository<Downloads, Integer> {

}
