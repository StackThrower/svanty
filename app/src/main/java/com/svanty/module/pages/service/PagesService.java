package com.svanty.module.pages.service;

import com.svanty.db.page.Paged;
import com.svanty.db.page.Paging;
import com.svanty.module.pages.db.PagesRepository;
import com.svanty.module.pages.db.entity.Pages;
import com.svanty.module.stock.db.CategoriesRepository;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.entity.Categories;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.model.CategoryGroupModel;
import com.svanty.module.stock.model.CategoryItemModel;
import com.svanty.module.stock.model.ImageItemModel;
import com.svanty.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagesService extends PaginationService<PagesRepository, Pages> {

    PagesRepository pagesRepository;

    @Autowired
    PagesService(PagesRepository pagesRepository) {
        super(pagesRepository);

        this.pagesRepository = pagesRepository;
    }

    public Paged<Pages> findAll(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Pages> postPage = pagesRepository.findAll(request);

        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), postPage.getTotalElements(), pageNumber, size));
    }


    public Pages findBySlug(String slug) {

        Pages page = pagesRepository.findBySlug(slug);

        return page;
    }




}
