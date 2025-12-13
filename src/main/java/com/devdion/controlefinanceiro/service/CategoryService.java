package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.CategoryResponseDTO;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.CategoryRepositoy;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepositoy categoryRepository;
    private final UserContextService userContextService;

    public CategoryService(CategoryRepositoy categoryRepositoy, UserContextService userContextService) {
        this.categoryRepository = categoryRepositoy;
        this.userContextService = userContextService;
    }


    public CategoryResponseDTO create(CategoryRequestDTO request) {
        User user = userContextService.getCurrentUser();

        if (categoryRepository.existsByNameAndUserAndType(request.name(), user, request.type())) {
            throw new RuntimeException("A categoria já existe");
        }

        Category category = new Category();
        category.setName(request.name());
        category.setType(request.type());
        category.setUser(user);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findByIdAndUser(request.parentId(), user);

            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return toResponseDTO(saved);

    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getParent().getId()
        );
    }
}
