package com.api.finances_backend.services.mapper;

import com.api.finances_backend.dtos.TransactionRequest;
import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Goal;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final AuthService authService;
    private final GoalRepository goalRepository;

    // Convertir una transacción a DTO
    public TransactionRequest toDto(Transaction transaction) {
        return TransactionRequest.builder()
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .amountGoal(transaction.getAmountGoal())
                .category(transaction.getCategory())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .paymentMethod(transaction.getPaymentMethod())
                .recurrent(transaction.getRecurrent())
                .frequency(transaction.getFrequency())
                .goalId(transaction.getGoal() != null ? transaction.getGoal().getId() : null)
                .build();
    }

    // Convertir un DTO a una transacción
    public Transaction toEntity(TransactionRequest transactionDTO) {
        User user = authService.getCurrentUser();
        Goal goal = transactionDTO.getGoalId() != null ? goalRepository.findById(transactionDTO.getGoalId()).orElse(null) : null;

        return Transaction.builder()
                .type(transactionDTO.getType())
                .amount(transactionDTO.getAmount())
                .amountGoal(transactionDTO.getAmountGoal())
                .category(transactionDTO.getCategory())
                .date(transactionDTO.getDate())
                .description(transactionDTO.getDescription())
                .paymentMethod(transactionDTO.getPaymentMethod())
                .recurrent(transactionDTO.getRecurrent())
                .frequency(transactionDTO.getFrequency())
                .goal(goal)
                .user(user)
                .build();
    }
}