package com.api.finances_backend.controllers;

import com.api.finances_backend.model.Goal;
import com.api.finances_backend.services.CategoryService;
import com.api.finances_backend.services.GoalService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @GetMapping("/user/{userId}")
    public List<Goal> getGoalsByUser(@PathVariable Long userId) {
        return goalService.getGoalsByUser(userId);
    }

    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    @PutMapping("/{goalId}/update-progress")
    public void updateGoalProgress(@PathVariable Long goalId){
        goalService.updateGoalProgress(goalId);
    }

    @DeleteMapping("/{goalId}")
    public  void deleteGoal(@PathVariable Long goalId){
        goalService.deleteGoal(goalId);
    }

}
