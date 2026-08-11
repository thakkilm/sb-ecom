package com.ecommerce.project.sbecom.service;

import com.ecommerce.project.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.project.sbecom.model.Category;
import com.ecommerce.project.sbecom.model.Product;
import com.ecommerce.project.sbecom.payload.ProductDTO;
import com.ecommerce.project.sbecom.payload.ProductResponse;
import com.ecommerce.project.sbecom.repository.CategoryRespository;
import com.ecommerce.project.sbecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {
        Category category = categoryRespository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category","CatgeoryID",categoryId));
        Product product = modelMapper.map(productDto,Product.class);

        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts() {
       List<ProductDTO> productDTOS= productRepository.findAll().stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        return new ProductResponse(productDTOS);
    }

    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId) {
        Optional<Category> category=categoryRespository.findById(categoryId);
        List<Product> products=productRepository.findByCategoryOrderByPriceAsc(category);
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
//        productNew.setCategory(product.getCategory());
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

        String path="images/";
        String fileName = uploadImage(path,image);
        product.setImage(fileName);
        productRepository.save(product);
        ProductDTO productDTO=modelMapper.map(product,ProductDTO.class);
        return productDTO;

    }

    private String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFileName = image.getOriginalFilename();
        String randomId= UUID.randomUUID().toString();
        String pathName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath=path+ File.separator+pathName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return pathName;
    }
}
