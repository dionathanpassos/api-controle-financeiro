package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.CategoryResponseDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO request, User user) {

        Category category = new Category();

        category.setName(request.name());
        category.setType(request.type());
        category.setUser(user);

        return category;
    }

    public CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getParent() != null
                        ? category.getParent().getId()
                        : null
        );
    }
}
