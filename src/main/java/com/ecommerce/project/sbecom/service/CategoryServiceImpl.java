package com.ecommerce.project.sbecom.service;

import com.ecommerce.project.sbecom.exceptions.APIException;
import com.ecommerce.project.sbecom.exceptions.NoCategoriesPresentException;
import com.ecommerce.project.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.payload.CategoryDTO;
import com.ecommerce.project.sbecom.payload.CategoryResponse;
import com.ecommerce.project.sbecom.repository.CategoryRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRespository categoryRespository;

    @Override
    public CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categories=categoryRespository.findAll(pageable);
        List<Category> categoryList=categories.getContent();
        List<CategoryDTO> categoryDTOList=categoryList.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        if(categoryList.isEmpty()){
            throw new NoCategoriesPresentException("No categories present");
        }
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setCategoryDTOList(categoryDTOList);
        categoryResponse.setTotalElements(categories.getTotalElements());
        categoryResponse.setTotalPages(categories.getTotalPages());
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setPageSize(categories.getSize());
        categoryResponse.setLastPage(categories.isLast());
        return categoryResponse;
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {

        Category category1 = categoryRespository.findByCategoryName(categoryDTO.getCategoryName());
        if(category1!=null){
            throw new APIException("Category "+categoryDTO.getCategoryName()+" already exists");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRespository.save(category);

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        Category category=categoryRespository.findAll().stream().filter(c->c.getCategoryId().equals(categoryId)).findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));
        CategoryDTO categoryDTO=modelMapper.map(category,CategoryDTO.class);
        categoryRespository.delete(category);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long categoryId) {

        Category existingCategory = categoryRespository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));
        existingCategory.setCategoryName(category.getCategoryName());
        categoryRespository.save(existingCategory);

        CategoryDTO categoryDTO=modelMapper.map(existingCategory,CategoryDTO.class);

        return categoryDTO;
    }
}
