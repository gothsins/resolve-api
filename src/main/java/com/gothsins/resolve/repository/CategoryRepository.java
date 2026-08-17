package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
