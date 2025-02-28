package com.api.finances_backend.services;

import com.api.finances_backend.dtos.GoalRequest;
import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Goal;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final AuthService authService;
    private final TransactionRepository transactionRepository;

    // Método para convertir Goal a GoalDto
    private GoalRequest convertToDto(Goal goal) {
        return GoalRequest.builder()
                .id(goal.getId())
                .description(goal.getDescription())
                .deadLine(goal.getDeadLine())
                .currentAmount(goal.getCurrentAmount())
                .targetAmount(goal.getTargetAmount())
                .status(goal.getStatus())
                .build();
    }

    // Método para convertir GoalDto a Goal
    private Goal convertToEntity(GoalRequest goalDto) {
        Goal goal = new Goal();
        goal.setDescription(goalDto.getDescription());
        goal.setDeadLine(goalDto.getDeadLine());
        goal.setCurrentAmount(goalDto.getCurrentAmount());
        goal.setTargetAmount(goalDto.getTargetAmount());
        goal.setStatus(goalDto.getStatus());
        return goal;
    }

    // Actualizar meta segun su id
    public List<GoalRequest> getUserGoals() {
        User user = authService.getCurrentUser(); // Obtener usuario autenticado
        List<Goal> goals = goalRepository.findByUserId(user.getId());

        // Actualizar el estado de cada meta antes de devolverla
        goals.forEach(this::updateGoalStatus);

        return goals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<GoalRequest> getGoalById(Long id) {
        User user = authService.getCurrentUser();
        Optional<Goal> goalOptional = goalRepository.findById(id)
                .filter(goal -> goal.getUser().getId().equals(user.getId())); // Verificar dueño

        // Si la meta existe, actualizar su estado antes de devolverla
        goalOptional.ifPresent(this::updateGoalStatus);

        return goalOptional.map(this::convertToDto); // Convertir a GoalDto
    }

    //Crear una meta
    public GoalRequest createGoal(GoalRequest goalDto) {
        User user = authService.getCurrentUser();
        Goal goal = convertToEntity(goalDto);
        goal.setUser(user);

        // Guardar la meta en la base de datos
        Goal savedGoal = goalRepository.save(goal);

        // Actualizar el estado de la meta (por si acaso)
        updateGoalStatus(savedGoal);

        return convertToDto(savedGoal); // Devolver GoalDto
    }

    // Actualizar una meta si pertenece al usuario autenticado
    public GoalRequest updateGoal(Long id, GoalRequest updatedGoalDto) {
        User user = authService.getCurrentUser();
        Goal existingGoal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        if (!existingGoal.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tiene permiso para modificar esta meta");
        }

        // Actualiza los campos de la meta
        if (updatedGoalDto.getCurrentAmount() != 0) {
            existingGoal.setCurrentAmount(updatedGoalDto.getCurrentAmount());
        }

        // Evita que el monto actual sea mayor que el objetivo
        if (updatedGoalDto.getTargetAmount() > 0 && updatedGoalDto.getTargetAmount() < existingGoal.getCurrentAmount()) {
            throw new RuntimeException("El monto objetivo no puede ser menor que el monto actual acumulado.");
        }

        if (updatedGoalDto.getTargetAmount() != 0) {
            existingGoal.setTargetAmount(updatedGoalDto.getTargetAmount());
        }

        if (updatedGoalDto.getDeadLine() != null) {
            existingGoal.setDeadLine(updatedGoalDto.getDeadLine());
        }

        if (updatedGoalDto.getDescription() != null) {
            existingGoal.setDescription(updatedGoalDto.getDescription());
        }

        // Guardar la meta actualizada
        Goal updatedGoal = goalRepository.save(existingGoal);

        // Actualizar el estado de la meta
        updateGoalStatus(updatedGoal);

        return convertToDto(updatedGoal); // Devolver GoalDto actualizado
    }

    // Método para actualizar el estado de la meta
    private void updateGoalStatus(Goal goal) {
        LocalDate currentDate = LocalDate.now();

        // Primero, si la meta ya se alcanzó o superó, marcarla como COMPLETED
        if (goal.getCurrentAmount() >= goal.getTargetAmount()) {
            System.out.println("Meta alcanzada o superada, marcando como COMPLETED");
            goal.setStatus(Goal.GoalStatus.COMPLETED);
            return; // No es necesario evaluar más condiciones
        }

        // Si la fecha límite ha pasado y la meta no se alcanzó, marcar como NOT_ACHIEVED
        if (goal.getDeadLine() != null && goal.getDeadLine().isBefore(currentDate)) {
            System.out.println("Fecha límite pasada, marcando como NOT_ACHIEVED");
            goal.setStatus(Goal.GoalStatus.NOT_ACHIEVED);
        } else {
            System.out.println("Meta aún en progreso");
            goal.setStatus(Goal.GoalStatus.IN_PROGRESS);
        }
    }

    // Método para actualizar el progreso de una meta
    public void updateGoalProgress(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        List<Transaction> transactions = transactionRepository.findByGoalId(goalId);

        // Verificar si hay transacciones asociadas
        if (transactions.isEmpty()) {
            System.out.println("No hay transacciones asociadas a la meta.");
            return;
        }

        // Sumar todos los montos de las transacciones relacionadas con la meta
        double total = transactions.stream()
                .mapToDouble(Transaction::getAmountGoal)
                .sum();

        // Actualizar el monto actual de la meta
        goal.setCurrentAmount(total);

        System.out.println("Nuevo monto acumulado para la meta: " + goal.getCurrentAmount());

        // Verificar el monto actual antes de actualizar el estado
        System.out.println("Monto actual: " + goal.getCurrentAmount() + ", Monto objetivo: " + goal.getTargetAmount());

        // Actualizar el estado de la meta
        updateGoalStatus(goal);

        System.out.println("Nuevo estado de la meta: " + goal.getStatus());

        // Guardar los cambios en la base de datos
        goalRepository.save(goal);
    }


    // Eliminar una meta si pertenece al usuario autenticado
    public void deleteGoal(Long id) {
        User user = authService.getCurrentUser();
        Goal goal = goalRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Meta no encontrada o no autorizada"));
        goalRepository.delete(goal);
    }
}
