package com.ecommerce.project.service;

import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);



    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
}
