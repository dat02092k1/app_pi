package com.project.shopapp.services.product;

import com.project.shopapp.dtos.product.ProductDTO;
import com.project.shopapp.dtos.product.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.product.ProductResponse;
import com.project.shopapp.utils.ConstantsUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(
                        () -> new DataNotFoundException("Can not find category with id: " + productDTO.getCategoryId())
                );
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existCategory)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new DataNotFoundException("Can not find product with id: " + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);

        if (existingProduct != null) {
            // copy all properties from productDTO to existingProduct
            // can use object mapper || ModelMapper
            Category existCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(
                            () -> new DataNotFoundException("Can not find category with id: " + productDTO.getCategoryId())
                    );

            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setCategory(existCategory);
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(
                        () -> new DataNotFoundException("Can not find category with id: " + productImageDTO.getProductId())
                );

        ProductImage newProductImage = ProductImage
                .builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // not allow to save > 5 images for a product
        int size = productImageRepository.findByProductId(productId).size();

        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
            throw new InvalidParamException("Can not save more than 5 images for a product");

        return productImageRepository.save(newProductImage);
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public void deleteFile(String filename) throws IOException {
        Path uploadDir = Paths.get(ConstantsUtils.UPLOADS_FOLDER);

        Path filePath = uploadDir.resolve(filename);

        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("File not found: " + filename);
        }
    }
}
