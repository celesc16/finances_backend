package com.api.finances_backend.services;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Category;
import com.api.finances_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    //Obtiene todas las categorias del usuario autenticado
    public List<Category> getAllCategories() {
        Long userId = authService.getCurrentUserId();
        return categoryRepository.findByUserId(userId);
    }

    //Obtiene una categoria por id(si pertenece al user autenticado)
    public Category getCategoryById(Long id){
        Long userId = authService.getCurrentUserId();
        return categoryRepository.findByIdAndUserId(id , userId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }

    //Crear categoria
    public Category createCategory(Category category) {
        User user = authService.getCurrentUser();
        category.setUser(user);
        return categoryRepository.save(category);
    }

    //Actualizar Una categoria
    public Category updateCategory(Long id, Category category) {
        Long userId = authService.getCurrentUserId();
        Category existingCategory = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    //Eliminar una categoria
    public void deleteCategory(Long id) {
        Long userId = authService.getCurrentUserId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoryRepository.delete(category);
    }
}
