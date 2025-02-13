package com.api.finances_backend.services;

import com.api.finances_backend.model.Category;
import com.api.finances_backend.model.Goal;
import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.repository.TransactionRepository;
import com.api.finances_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final TransactionRepository transactionRepository;

    //Obtener todas las metas de un usuario
    public List<Goal> getGoalsByUser(Long userId) {
        return goalRepository.findByUserId(userId);
    }

    //Crear una meta
    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    //Actulizar progreso de meta
    public void updateGoalProgress(Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        List<Transaction> transactions = transactionRepository.findByGoalId(goalId);

        double total = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        goal.setCurrentAmount(total);
        goalRepository.save(goal);
    }

    //Elimar una meta
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }


}
