package com.svanty.service;

import com.svanty.db.page.Paged;
import com.svanty.db.page.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public class PaginationService<T extends JpaRepository, K> {

    T repository;

    public PaginationService(T repository) {
        this.repository = repository;
    }


    public Paged<K> getPage(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<K> postPage = repository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), postPage.getTotalElements(), pageNumber, size));

    }

    public long count() {
        return repository.count();
    }

    public K getById(Integer id) {
        return (K) repository.getById(id);
    }

    public List<K> findAll() {
        return repository.findAll();
    }

    public List<K> findAll(Sort sort) {
        return repository.findAll(sort);
    }

}
