package com.api.finances_backend.controllers;


import com.api.finances_backend.model.Category;
import com.api.finances_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;

    //Obtiene todas las categorias
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //Obtiene las categorias por id
    @GetMapping("/{id}")
    public Category getCategoryId(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    //Crear una categoria
    @PostMapping
    public Category createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    //Actualiza una categoria
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category){
        return categoryService.updateCategory(id , category);
    }

    //Elimina una categoria
    @DeleteMapping("/{id}")
    public void  deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

}
