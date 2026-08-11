package com.ecommerce.project.sbecom.service;

import com.ecommerce.project.sbecom.exceptions.APIException;
import com.ecommerce.project.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.model.Product;
import com.ecommerce.project.sbecom.payload.ProductDTO;
import com.ecommerce.project.sbecom.payload.ProductResponse;
import com.ecommerce.project.sbecom.repository.CategoryRespository;
import com.ecommerce.project.sbecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRespository categoryRespository;

    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    private FileService fileService;

    @Value("${project.path}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {
        Category category = categoryRespository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category","CatgeoryID",categoryId));
        boolean isProductNotPresent=true;
       List<Product> products=category.getProducts();
       for(int i=0;i<products.size();i++){

           if(products.get(i).getProductName().equals(productDto.getProductName())){
               isProductNotPresent=false;
               break;
           }
       }
       if(isProductNotPresent) {
           Product product = modelMapper.map(productDto, Product.class);

           product.setCategory(category);
           product.setImage("default.png");
           double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
           product.setSpecialPrice(specialPrice);
           Product savedProduct = productRepository.save(product);
           return modelMapper.map(product, ProductDTO.class);
       }else{
           throw new APIException("Product Already Exists");
       }
    }

    @Override
    public ProductResponse getAllProducts() {
       List<ProductDTO> productDTOS= productRepository.findAll().stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
       if(productDTOS.isEmpty()){
           throw new APIException("Product Not Exists");
       }
        return new ProductResponse(productDTOS);
    }

    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId) {
        Optional<Category> category= Optional.of(categoryRespository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId)));
        List<Product> products=productRepository.findByCategoryOrderByPriceAsc(category);
        if(products.isEmpty()){
            throw new APIException("Product Not Exists");
        }
        ProductResponse productResponse=new ProductResponse();
        List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        productResponse.setProducts(productDTOS);
        return productResponse;

    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products=productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%");
        System.out.println("implemented");
        ProductResponse productResponse=new ProductResponse();
        List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        productResponse.setProducts(productDTOS);
        return productResponse;

    }

    @Override
    public ProductDTO updateProductByProductId(ProductDTO productDto, Long productId) {
        Product productNew=productRepository.findByProductId(productId);
        if(productNew==null){
            throw new ResourceNotFoundException("Product","ProductID",productId);
        }
        Product product=modelMapper.map(productDto,Product.class);
        productNew.setProductName(product.getProductName());
        productNew.setPrice(product.getPrice());
        productNew.setSpecialPrice(product.getSpecialPrice());
        productNew.setImage("default.png");
        productNew.setDiscount(product.getDiscount());
        productRepository.save(productNew);
        return modelMapper.map(productNew,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProductByProductId(Long productId) {
       Product product= productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ProductID",productId));
       productRepository.delete(product);
       ProductDTO productDTO=modelMapper.map(product,ProductDTO.class);
       return productDTO;
    }

    @Override
    public ProductDTO udpateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product= productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ProductID",productId));
        String fileName = fileService.uploadImage(path,image);
        product.setImage(fileName);
        productRepository.save(product);
        ProductDTO productDTO=modelMapper.map(product,ProductDTO.class);
        return productDTO;

    }


}
