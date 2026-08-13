package com.ecommerce.project.sbecom.controller;

import com.ecommerce.project.sbecom.config.AppConstants;
import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.payload.CategoryDTO;
import com.ecommerce.project.sbecom.payload.CategoryResponse;
import com.ecommerce.project.sbecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategoriesList(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                 @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY)  String sortBy,
                                                                 @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

       CategoryResponse categoryResponse = categoryService.getCategoryList(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);

    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO category, @PathVariable Long categoryId) {
        CategoryDTO categoryResponse = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);

    }
}

