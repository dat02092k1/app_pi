package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import com.project.shopapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
