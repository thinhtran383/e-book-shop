package org.example.bookshop.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.category.CategoryDto;
import org.example.bookshop.dto.category.UpdateCategoryDto;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.Response;
import org.example.bookshop.responses.category.CategoriesResponse;
import org.example.bookshop.services.CategoryService;
import org.example.bookshop.utils.Exporter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.base-path}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<Response<PageableResponse<CategoriesResponse>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {

        PageableResponse<CategoriesResponse> response = categoryService.getAllCategories(page, limit);

        return ResponseEntity.ok(Response.<PageableResponse<CategoriesResponse>>builder()
                .data(response)
                .message("Get all categories success")
                .build());
    }


    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportCategoriesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categories.xlsx";
        response.setHeader(headerKey, headerValue);


        PageableResponse<CategoriesResponse> categoriesResponses = categoryService.getAllCategories(0, 1000);

        Exporter<CategoriesResponse> exporter = new Exporter<>(categoriesResponses.getElements(), "Categories");

        exporter.export(response);

    }

    @PostMapping
    public ResponseEntity<Response<CategoriesResponse>> createNewCategory(
            @RequestBody CategoryDto category
    ) {
        return ResponseEntity.ok(Response.<CategoriesResponse>builder()
                .data(categoryService.createNewCategory(category))
                .message("Create new category success")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteCategory(
            @PathVariable Integer id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Response.<String>builder()
                .data(null)
                .message("Delete category success")
                .build());
    }

    @PutMapping("/update-category/{id}")

    public ResponseEntity<Response<CategoriesResponse>> updateCategory(
            @PathVariable Integer id,
            @RequestBody UpdateCategoryDto category
    ) {
        return ResponseEntity.ok(Response.<CategoriesResponse>builder()
                .data(categoryService.updateCategory(id, category))
                .message("Update category success")
                .build());
    }

}
