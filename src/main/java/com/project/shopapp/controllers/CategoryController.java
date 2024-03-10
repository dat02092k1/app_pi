package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.components.converters.CategoryMessageConverter;
import com.project.shopapp.dtos.category.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.responses.category.UpdateCategoryResponse;
import com.project.shopapp.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
// Dependency injection
@RequiredArgsConstructor
//@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMsg = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMsg.toString());
        }

        Category category = categoryService.createCategory(categoryDTO);

        this.kafkaTemplate.send("insert-a-category", category); // produce a message to the topic: producer

        kafkaTemplate.setMessageConverter(new CategoryMessageConverter());

        return ResponseEntity.ok("Inserted category");
    }

    // display all categories
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        kafkaTemplate.send("get-all-categories", categories); // produce a message to the topic: producer

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(category)
                        .message("Category retrieved")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
            ) {
        Category category = categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Category updated successfully")
                        .status(HttpStatus.OK)
                        .data(category)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable("id") Long id) throws Exception {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Category deleted successfully")
                        .build()
        );
    }
}
