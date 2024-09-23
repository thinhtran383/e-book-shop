package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.CategoryDto;
import org.example.bookshop.entities.Category;
import org.example.bookshop.repositories.ICategoryRepository;
import org.example.bookshop.responses.category.CategoriesResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<CategoriesResponse> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);

        return categories.map(category -> modelMapper.map(category, CategoriesResponse.class));
    }

    @Transactional
    public CategoriesResponse createNewCategory(CategoryDto category) {
        Category newCategory = modelMapper.map(category, Category.class);

        return modelMapper.map(categoryRepository.save(newCategory), CategoriesResponse.class);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
