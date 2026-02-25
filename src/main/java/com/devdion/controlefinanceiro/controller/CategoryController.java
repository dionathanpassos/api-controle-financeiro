package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.category.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryResponseDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryTreeResponseDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryUpdateRequestDTO;
import com.devdion.controlefinanceiro.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO created = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CategoryTreeResponseDTO>> findCategoryTree() {
        return ResponseEntity.ok(categoryService.findCategoryTree());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findByIdAndUser(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequestDTO request) {
        CategoryResponseDTO response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CategoryResponseDTO> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryResponseDTO> activate(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.activate(id));
    }

    @PatchMapping("/{id}/activateAll")
    public ResponseEntity<String> activateAll(@PathVariable Long id) {
        categoryService.activateAll(id);
        return ResponseEntity.ok("Subcategorias reativadas com sucesso");
    }
}
