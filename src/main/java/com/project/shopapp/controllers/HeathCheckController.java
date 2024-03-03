package com.project.shopapp.controllers;

import com.project.shopapp.services.category.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
@RequestMapping("${api.prefix}/health-check")
@AllArgsConstructor
public class HeathCheckController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public Health healthCheck() {
        try {
            String computerName = InetAddress.getLocalHost().getHostName();
            return Health.up().withDetail("Computer Name", computerName).build();
        }
        catch (Exception e) {
            return Health.down().withDetail("Error", e.getMessage()).build();
        }
    }
}
