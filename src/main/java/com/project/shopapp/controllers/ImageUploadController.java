package com.project.shopapp.controllers;

import com.project.shopapp.dtos.image.ImageDTO;
import com.project.shopapp.services.image.CloudinaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageUploadController {
    @Autowired
    private CloudinaryServiceImpl cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String secureUrl = cloudinaryService.uploadFile(file,"Avatar user");
            return ResponseEntity.ok(Map.of("url", secureUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
