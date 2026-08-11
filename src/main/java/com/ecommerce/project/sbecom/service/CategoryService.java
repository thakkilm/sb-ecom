package com.ecommerce.project.sbecom.service;

import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.payload.CategoryDTO;
import com.ecommerce.project.sbecom.payload.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
    void createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);



    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
}
