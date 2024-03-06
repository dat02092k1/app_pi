package com.project.shopapp.repositories;

import com.project.shopapp.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
