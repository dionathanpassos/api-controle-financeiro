package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.category.CategoryRequestDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryResponseDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryTreeResponseDTO;
import com.devdion.controlefinanceiro.dto.category.CategoryUpdateRequestDTO;
import com.devdion.controlefinanceiro.exception.BusinessException;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.mapper.CategoryMapper;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.CategoryStatus;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.CategoryRepository;
import com.devdion.controlefinanceiro.security.AuthenticatedUserService;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepositoy,
                           AuthenticatedUserService authenticatedUserService1,
                           CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepositoy;
        this.authenticatedUserService = authenticatedUserService1;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO create(CategoryRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        if (categoryRepository.existsByNameAndUserAndType(request.name(), user, request.type())) {
            throw new BusinessException("A categoria já existe");
        }

        Category category = categoryMapper.toEntity(request, user);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findByIdAndUser(request.parentId(), user)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

            if (request.type() != parent.getType()) {
                throw new BusinessException("Subcategoria precisa ser vinculada a uma categoria do mesmo tipo");
            }

            if (parent.getId().equals(category.getId())) {
                throw new BusinessException("Categorias pai nao podem ser subcategoria");
            }
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.fromEntity(saved);

    }

    public CategoryResponseDTO update(Long id, CategoryUpdateRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();
        System.out.println(request);

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if (category.getStatus() == CategoryStatus.DELETED) {
            throw new BusinessException("Categoria bloqueada, não é possível alterar");
        }

        if (request.parentId() != null) {
            Category parent = categoryRepository.findByIdAndUser(request.parentId(), user)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

            if (category.getType() != parent.getType()) {
                throw new BusinessException("Subcategoria precisa ser vinculada a uma categoria do mesmo tipo");
            }

            if (parent.getId().equals(category.getId())) {
                throw new BusinessException("Categorias pai nao podem ser subcategoria");
            }
            category.setParent(parent);
        }

        categoryMapper.updateToEntity(request, category);
        Category saved = categoryRepository.save(category);
        return categoryMapper.fromEntity(saved);
    }

    @Transactional
    public CategoryResponseDTO deactivate (Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if (category.getStatus() == CategoryStatus.DELETED){
            throw new BusinessException("A categoria já está desativada");
        }

        if (category.getParent() == null) {
            List<Category> children = categoryRepository.findByParentIdAndUser(id, user);
            children.forEach(child -> {
                child.setStatus(CategoryStatus.DELETED);
                child.setDeletedAt(LocalDateTime.now());
            });
        }

        category.setStatus(CategoryStatus.DELETED);
        category.setDeletedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);
        return categoryMapper.fromEntity(saved);
    }

    public CategoryResponseDTO activate (Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if (category.getStatus() == CategoryStatus.ACTIVE){
            throw new BusinessException("A categoria já est[a desativada");
        }
        if (category.getParent() != null) {
            Category parent = categoryRepository.findByIdAndUser(category.getParent().getId(), user)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

            if (parent.getStatus() == CategoryStatus.DELETED) {
                throw new BusinessException("Não é possível ativar uma subcategoria com categoria pai inativa");
            }
        }

        category.setStatus(CategoryStatus.ACTIVE);
        category.setDeletedAt(null);

        Category saved = categoryRepository.save(category);
        return categoryMapper.fromEntity(saved);
    }

    @Transactional
    public void activateAll (Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if (category.getStatus() == CategoryStatus.DELETED){
            throw new BusinessException("Não é possível reativar subcategorias com a categoria pai desativada");
        }

        if (category.getParent() == null) {
            List<Category> children = categoryRepository.findByParentIdAndUser(id, user);
            children.forEach(child -> {
                child.setStatus(CategoryStatus.ACTIVE);
                child.setDeletedAt(null);
            });
        }

    }

    public CategoryResponseDTO findById(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));
        return categoryMapper.fromEntity(category);
    }

    public List<CategoryTreeResponseDTO> findCategoryTree() {
        User user = authenticatedUserService.getAuthenticatedUser();

        List<Category> categories = categoryRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        Map<Long, CategoryTreeResponseDTO> parents = new HashMap<>();

        categories.stream()
                .filter(c -> c.getParent() == null)
                .forEach(c -> parents.put(
                        c.getId(),
                        new CategoryTreeResponseDTO(
                                c.getId(),
                                c.getName(),
                                c.getType(),
                                c.getStatus(),
                                c.getDeletedAt(),
                                new ArrayList<>()
                        )));

        categories.stream()
                .filter(c -> c.getParent() != null)
                .forEach(c -> {
                    CategoryTreeResponseDTO parent = parents.get(c.getParent().getId());

                    if (parent != null) {
                        parent.subcategories().add(
                                new CategoryResponseDTO(
                                        c.getId(),
                                        c.getName(),
                                        c.getType(),
                                        c.getParent().getId(),
                                        c.getStatus(),
                                        c.getDeletedAt()
                                )
                        );
                    }
                });

        return new ArrayList<>(parents.values());

    }
}
