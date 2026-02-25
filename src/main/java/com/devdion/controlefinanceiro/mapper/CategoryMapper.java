package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.category.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryResponseDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryUpdateRequestDTO;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO request, User user) {

        Category category = new Category();

        category.setName(request.name());
        category.setType(request.type());
        category.setUser(user);

        return category;
    }

    public Category updateToEntity(CategoryUpdateRequestDTO request, Category category) {

        category.setName(request.name());
        category.setType(request.type());
        return category;
    }

    public CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getStatus(),
                category.getDeletedAt()
        );
    }

    public List<CategoryResponseDTO> fromEntity(List<Category> categories) {
        return categories.stream()
                .map(this::fromEntity)
                .toList();
    }


}
