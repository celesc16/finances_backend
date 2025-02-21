package com.api.finances_backend.controllers;

import com.api.finances_backend.model.Goal;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.services.GoalService;
import com.api.finances_backend.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final TransactionService transactionService;
    private final GoalService goalService;

    //Obtenie todas las transactions
    @GetMapping
    public List<Goal> getGoals() {
        return goalService.getUserGoals();
    }

    @GetMapping("/{id}")
    public Goal getGoalById(@PathVariable Long id) {
        return goalService.getGoalById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }

    //Crea transacciones
    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    //Actualiza transacciones
    @PutMapping("/{id}")
    public Goal updateGoal(@PathVariable Long id , @RequestBody Goal goal){
        return goalService.updateGoal(id, goal);
    }

    //Elimina transacciones
    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
    }
}
