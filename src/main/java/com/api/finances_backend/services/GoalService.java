package com.api.finances_backend.services;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Goal;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {
    private  final GoalRepository goalRepository;
    private  final AuthService authService;
    private  final TransactionRepository transactionRepository;

    // Obtener todas las Metas del usuario autenticado
    public List<Goal> getUserGoals() {
        User user = authService.getCurrentUser(); // Obtener usuario autenticado
        return goalRepository.findByUserId(user.getId());
    }

    // Obtener una metas por ID del usuario autenticado
    public Optional<Goal> getGoalById(Long id) {
        User user = authService.getCurrentUser();
        return goalRepository.findById(id)
                .filter(transaction -> transaction.getUser().getId().equals(user.getId())); // Verificar dueño
    }

    // Crear una nueva metas para el usuario autenticado
    public Goal createGoal(Goal goal) {
        User user = authService.getCurrentUser();
        goal.setUser(user);
        return goalRepository.save(goal);
    }

    // Actualizar una metas si pertenece al usuario autenticado
    public Goal updateGoal(Long id, Goal updatedGoal) {
        User user = authService.getCurrentUser();
        Goal existingGoal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
        if(!existingGoal.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tiene permiso para moodifcar esta transaccion");
        }
        //Actuliza los campos de la transaccion
        if(updatedGoal.getCurrentAmount() != 0){
            existingGoal.setCurrentAmount(updatedGoal.getCurrentAmount());
        }

        if(updatedGoal.getTargetAmount() != 0){
            existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        }

        if(updatedGoal.getName() != null){
            existingGoal.setName(updatedGoal.getName());
        }

        if(updatedGoal.getDeadLine() != null){
            existingGoal.setDeadLine(updatedGoal.getDeadLine());
        }

        if(updatedGoal.getDescription() != null){
            existingGoal.setDescription(updatedGoal.getDescription());
        }

        return  goalRepository.save(existingGoal);
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


    // Eliminar una metas si pertenece al usuario autenticado
    public void deleteGoal(Long id) {
        User user = authService.getCurrentUser();
        Goal goal = goalRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada o no autorizada"));
        goalRepository.delete(goal);
    }
}