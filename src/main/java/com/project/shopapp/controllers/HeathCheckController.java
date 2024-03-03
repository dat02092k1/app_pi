package com.project.shopapp.controllers;

import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.services.category.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("${api.prefix}/health-check")
@AllArgsConstructor
public class HeathCheckController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> healthCheck() throws UnknownHostException {
            String computerName = InetAddress.getLocalHost().getHostName();
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Health check successful")
                            .status(HttpStatus.OK)
                            .data(Health.up().withDetail("Computer Name", computerName).build())
                            .build()
            );
    }
}
