package com.ecommerce.project.sbecom.controller;

import com.ecommerce.project.sbecom.config.AppConstants;
import com.ecommerce.project.sbecom.payload.ProductDTO;
import com.ecommerce.project.sbecom.payload.ProductResponse;
import com.ecommerce.project.sbecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductDTO productDto,
                                                         @PathVariable Long categoryId) {

        ProductResponse productDTO= productService.addProduct(productDto,categoryId);
       return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY)  String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ){
        return   new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                    @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                    @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY)  String sortBy,
                                                                    @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder){
        ProductResponse productResponse= productService.getProductsByCategoryId(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return   new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> searchProductByKeyword(@PathVariable String keyword,
                                                                  @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                  @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                  @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY)  String sortBy,
                                                                  @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder){
        ProductResponse productResponse= productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return   new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductByProductId(@Valid  @RequestBody ProductDTO productDTO,
                                                                    @PathVariable Long productId){
        ProductDTO productResponse= productService.updateProductByProductId(productDTO, productId);
        return   new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProductByProductId(
                                                               @PathVariable Long productId){
        ProductDTO productDTO=productService.deleteProductByProductId( productId);
        return   new ResponseEntity<>( productDTO,HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> searchProductByProductId(@PathVariable Long productId,
                                                               @RequestParam MultipartFile image) throws IOException {
        ProductDTO productResponse= productService.udpateProductImage(productId,image);
        return   new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }
}
