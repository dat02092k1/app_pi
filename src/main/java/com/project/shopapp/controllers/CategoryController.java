package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.category.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.category.UpdateCategoryResponse;
import com.project.shopapp.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

        categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok("Inserted category");
    }

    // display all categories
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
            ) {
        categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(
                UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage("category.update_category.success"))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok("Category deleted");
    }
}
