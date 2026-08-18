package com.gothsins.resolve.service;


import com.gothsins.resolve.dto.CategoryRequestDTO;
import com.gothsins.resolve.dto.CategoryResponseDTO;
import com.gothsins.resolve.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryService categoryService;

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category saved = categoryRepository.save(category);
        return toResponseDTO(saved);
    }
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada: id " + id));
        return toResponseDTO(category);
    }
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada: id " + id));

        category.setName(dto.getName());

        Category updated = categoryRepository.save(category);
        return toResponseDTO(updated);
    }
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada: id " + id));
        categoryRepository.delete(category);
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .active(category.getActive())
                .build();
    }

}
