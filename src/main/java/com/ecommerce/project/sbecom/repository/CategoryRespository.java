package com.ecommerce.project.sbecom.repository;

import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRespository extends JpaRepository<Category, Long> {
    Category findByCategoryName(@NotBlank String categoryName);


}
