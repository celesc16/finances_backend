package com.api.finances_backend.controllers;

import com.api.finances_backend.dtos.GoalRequest;
import com.api.finances_backend.services.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    // Obtener todas las metas del usuario autenticado
    @GetMapping
    public List<GoalRequest> getGoals() {
        return goalService.getUserGoals(); // El estado se actualiza automáticamente
    }

    // Obtener una meta por su ID
    @GetMapping("/{id}")
    public GoalRequest getGoalById(@PathVariable Long id) {
        return goalService.getGoalById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
    }

    // Crear una nueva meta
    @PostMapping
    public GoalRequest createGoal(@RequestBody GoalRequest goalDto) {
        return goalService.createGoal(goalDto);
    }

    // Actualizar una meta existente
    @PutMapping("/{id}")
    public GoalRequest updateGoal(@PathVariable Long id, @RequestBody GoalRequest goalDto) {
        return goalService.updateGoal(id, goalDto);
    }

    // Eliminar una meta
    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
    }
}