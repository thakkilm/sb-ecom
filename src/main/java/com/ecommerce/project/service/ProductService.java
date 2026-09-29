package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProductService {
    ProductResponse addProduct(ProductDTO productDto, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyword(String keyword,Integer pageNumber,Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProductByProductId(ProductDTO productDTO ,Long productId);

    ProductDTO deleteProductByProductId(Long productId);

    ProductDTO udpateProductImage(Long productId, MultipartFile multipartFile) throws IOException;

}
