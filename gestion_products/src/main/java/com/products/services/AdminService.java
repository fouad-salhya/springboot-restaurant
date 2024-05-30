package com.products.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.products.entities.ProductEntity;
import com.products.repository.ProductRepository;
import com.products.shared.Utils;

@Service
public class AdminService {
	
    private final String uploadDir = "src/main/resources/static/images/";
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	Utils utils;
	
    public List<ProductEntity> getAllProducts(int page, int limit) {
		
		if (page > 0) page -= 1;
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<ProductEntity> productPage = productRepository.findAll(pageableRequest);
		
		List<ProductEntity> products = productPage.getContent();
		
		return products;
	
	}
    
    public ProductEntity getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public void updateProduct(long id, ProductEntity product, MultipartFile image) throws IOException {
        ProductEntity existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setPrix(product.getPrix());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            if (!image.isEmpty()) {
                String imagePath = uploadDir + image.getOriginalFilename();
                existingProduct.setImagePath("/images/" + image.getOriginalFilename());
                Files.copy(image.getInputStream(), Paths.get(imagePath));
            }
            productRepository.save(existingProduct);
        }
    }

	public ProductEntity createProduct(ProductEntity product, MultipartFile image) throws IOException {
		
		product.setProductId(utils.generateStringId(32));
		
		 if (!image.isEmpty()) {
	            String imagePath = uploadDir + image.getOriginalFilename();
	            product.setImagePath("/images/" + image.getOriginalFilename());
	            Files.copy(image.getInputStream(), Paths.get(imagePath));
	        }
	       return productRepository.save(product);
	}
	
	public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
	
}
