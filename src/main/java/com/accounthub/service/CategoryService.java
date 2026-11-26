package com.accounthub.service;

import com.accounthub.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Page<Category> findByNameContaining(String keyword, Pageable page);

    List<Category> findByNameContaining(String keyword);

    Page<Category> findAll(Pageable pageable);

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(Category category);

    void deleteById(Long id);

    long count();
}
