package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.CategoryType;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepositoy extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);
    boolean existsByNameAndUserAndType(
            String name,
            User user,
            CategoryType type
    );

    Category findByIdAndUser(Long id, User user);
}
