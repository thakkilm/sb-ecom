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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    public ProductResponse addProduct(ProductDTO productDto, Long categoryId) {
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
           ProductDTO productDTO=  modelMapper.map(savedProduct, ProductDTO.class);
           List<ProductDTO> productDTOS=List.of(productDTO);
           return new ProductResponse(productDTOS);
       }else{
           throw new APIException("Product Already Exists");
       }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> pageProducts = productRepository.findAll(pageable);
        List<Product> products = pageProducts.getContent();
       List<ProductDTO> productDTOS= products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
       if(productDTOS.isEmpty()){
           throw new APIException("Product Not Exists");
       }

       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages((int) pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId,Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Optional<Category> category= Optional.of(categoryRespository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId)));
        Sort sort = sortBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageable);
        List<Product> products = pageProducts.getContent();
        if(products.isEmpty()){
            throw new APIException("Product Not Exists");
        }
        ProductResponse productResponse=new ProductResponse();
        List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages((int) pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;

    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword,Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortBy.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> pageProducts=productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%",pageable);
        List<Product> products = pageProducts.getContent();
        if(products.isEmpty()){
            throw new APIException("Product Not Exists");
        }
        ProductResponse productResponse=new ProductResponse();
        List<ProductDTO> productDTOS=products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages((int) pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
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
