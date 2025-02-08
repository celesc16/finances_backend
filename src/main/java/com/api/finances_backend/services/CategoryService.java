package com.api.finances_backend.services;

import com.api.finances_backend.model.Category;
import com.api.finances_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();  //Obtiene todas las categorias
    }

    //Obtiene una categoria por id
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }

    //Crear categoria
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    //Actualizar Una categoria
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    //Elimina una categoria
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

}
