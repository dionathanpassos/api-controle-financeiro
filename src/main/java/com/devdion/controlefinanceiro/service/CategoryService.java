package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.CategoryResponseDTO;
import com.devdion.controlefinanceiro.mapper.CategoryMapper;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.CategoryRepositoy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepositoy categoryRepository;
    private final UserContextService userContextService;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepositoy categoryRepositoy, UserContextService userContextService, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepositoy;
        this.userContextService = userContextService;
        this.categoryMapper = categoryMapper;
    }


    public CategoryResponseDTO create(CategoryRequestDTO request) {
        User user = userContextService.getCurrentUser();

        if (categoryRepository.existsByNameAndUserAndType(request.name(), user, request.type())) {
            throw new RuntimeException("A categoria já existe");
        }

        Category category = categoryMapper.toEntity(request, user);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findByIdAndUser(request.parentId(), user)
                    .orElseThrow(() -> new RuntimeException("Categoria pai nao encontrada"));
            if (request.type() != parent.getType()) {
                throw new RuntimeException("Subcategoria precisa ser vinculada a uma categoria do mesmo tipo");
            }
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.fromEntity(saved);

    }

    public List<CategoryResponseDTO> findAllByUser() {
        User user = userContextService.getCurrentUser();

        List<Category> categories = categoryRepository.findByUser(user);
        return categoryMapper.fromEntity(categories);
    }


}
