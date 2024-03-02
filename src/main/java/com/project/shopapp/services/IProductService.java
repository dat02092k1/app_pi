package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    void deleteFile(String filename) throws IOException;
}
