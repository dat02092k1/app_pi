package com.project.shopapp.controllers;

import com.project.shopapp.models.Category;
import com.project.shopapp.services.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/health-check")
@AllArgsConstructor
public class HeathCheckController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> healthCheck() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok("ok");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
