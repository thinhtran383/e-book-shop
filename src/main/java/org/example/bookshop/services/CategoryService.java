package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.category.CategoryDto;
import org.example.bookshop.dto.category.UpdateCategoryDto;
import org.example.bookshop.entities.Category;
import org.example.bookshop.repositories.ICategoryRepository;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.category.CategoriesResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#page")
    public PageableResponse<CategoriesResponse> getAllCategories(int page, int limit) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, limit));

        return PageableResponse.<CategoriesResponse>builder()
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .elements(categories.map(category -> modelMapper.map(category, CategoriesResponse.class)).getContent())
                .build();
    }


    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoriesResponse createNewCategory(CategoryDto category) {
        Category newCategory = modelMapper.map(category, Category.class);

        return modelMapper.map(categoryRepository.save(newCategory), CategoriesResponse.class);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoriesResponse updateCategory(Integer id, UpdateCategoryDto category) {
        Category categoryToUpdate = categoryRepository.findById(id).orElseThrow();

        if (category.getCategoryName() != null && !category.getCategoryName().isBlank()) {
            categoryToUpdate.setCategoryName(category.getCategoryName());
        }

        if (category.getDescription() != null && !category.getDescription().isBlank()) {
            categoryToUpdate.setDescription(category.getDescription());
        }


        return modelMapper.map(categoryRepository.save(categoryToUpdate), CategoriesResponse.class);
    }
}
