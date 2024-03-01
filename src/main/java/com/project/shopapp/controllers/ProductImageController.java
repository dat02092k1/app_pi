package com.project.shopapp.controllers;

import com.project.shopapp.models.ProductImage;
import com.project.shopapp.services.IProductImageService;
import com.project.shopapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/product_images")
@RequiredArgsConstructor
public class ProductImageController {
    private final IProductImageService productImageService;
    private final ProductService productService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProductImage(@PathVariable Long id) {
        try {
            ProductImage productImage = productImageService.deleteProductImage(id);

            if (productImage != null) {
                productService.deleteFile(productImage.getImageUrl());
            }

            return ResponseEntity.ok(productImageService.deleteProductImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
