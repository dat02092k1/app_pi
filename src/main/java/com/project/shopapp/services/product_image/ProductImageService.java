package com.project.shopapp.services.product_image;

import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.ProductImageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductImageService implements IProductImageService{
    private final ProductImageRepository productImageRepository;

    @Transactional
    @Override
    public ProductImage deleteProductImage(Long id) throws Exception {
        Optional<ProductImage> productImage = productImageRepository.findById(id);

        if (productImage.isEmpty()) throw new DataNotFoundException(
                String.format("Product image with id %d not found", id)
        );

        productImageRepository.deleteById(id);
        return productImage.get();
    }
}
