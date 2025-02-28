package com.api.finances_backend.services;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Goal;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.repository.TransactionRepository;
import com.api.finances_backend.services.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.api.finances_backend.dtos.TransactionRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final GoalRepository goalRepository;
    private final TransactionMapper transactionMapper;


    // Obtener todas las transacciones del usuario autenticado
    public List<TransactionRequest> getUserTransactions() {
        User user = authService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        return transactions.stream().map(transactionMapper::toDto).collect(Collectors.toList());
    }

    // Obtener una transacción por ID del usuario autenticado
    public Optional<TransactionRequest> getTransactionById(Long id) {
        User user = authService.getCurrentUser();
        return transactionRepository.findById(id)
                .filter(transaction -> transaction.getUser().getId().equals(user.getId())) // Verificar dueño
                .map(transactionMapper::toDto);
    }

    // Crear una nueva transacción para el usuario autenticado
    public TransactionRequest createTransaction(TransactionRequest transactionDTO) {
        User user = authService.getCurrentUser();
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setUser(user);

        // Si la transacción tiene un monto destinado a una meta, actualizar la meta
        if (transaction.getGoal() != null && transaction.getAmountGoal() > 0) {
            if (transaction.getAmountGoal() > transaction.getAmount()) {
                throw new RuntimeException("El monto destinado a la meta no puede ser mayor que el monto total de la transacción");
            }

            Goal goal = transaction.getGoal();
            goal.setCurrentAmount(goal.getCurrentAmount() + transaction.getAmountGoal());
            goalRepository.save(goal);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    // Actualizar una transacción si pertenece al usuario autenticado
    public TransactionRequest updateTransaction(Long id, TransactionRequest updatedTransactionDTO) {
        User user = authService.getCurrentUser();
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (!existingTransaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tiene permiso para modificar esta transacción");
        }

        // Si se actualiza el amountGoal, se ajusta el currentAmount de la meta
        if (existingTransaction.getAmountGoal() > 0) {
            Goal oldGoal = existingTransaction.getGoal();
            oldGoal.setCurrentAmount(oldGoal.getCurrentAmount() - existingTransaction.getAmountGoal()); // Restar el monto anterior
            goalRepository.save(oldGoal);
        }

        // Actualiza los campos de la transacción
        existingTransaction.setAmount(updatedTransactionDTO.getAmount());
        existingTransaction.setCategory(updatedTransactionDTO.getCategory());
        existingTransaction.setDate(updatedTransactionDTO.getDate());
        existingTransaction.setType(updatedTransactionDTO.getType());
        existingTransaction.setDescription(updatedTransactionDTO.getDescription());
        existingTransaction.setAmountGoal(updatedTransactionDTO.getAmountGoal());
        existingTransaction.setGoal(updatedTransactionDTO.getGoalId() != null ? goalRepository.findById(updatedTransactionDTO.getGoalId()).orElse(null) : null);
        existingTransaction.setPaymentMethod(updatedTransactionDTO.getPaymentMethod());

        // Si la transacción tiene un monto destinado a una meta, actualizamos el currentAmount de la meta
        if (updatedTransactionDTO.getAmountGoal() > 0) {
            Goal newGoal = existingTransaction.getGoal();
            newGoal.setCurrentAmount(newGoal.getCurrentAmount() + updatedTransactionDTO.getAmountGoal());
            goalRepository.save(newGoal);
        }

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(updatedTransaction);
    }

    // Eliminar una transacción si pertenece al usuario autenticado
    public void deleteTransaction(Long id) {
        User user = authService.getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada o no autorizada"));

        // Ajustar el currentAmount de la meta antes de eliminar la transacción
        if (transaction.getAmountGoal() > 0) {
            Goal goal = transaction.getGoal();
            goal.setCurrentAmount(goal.getCurrentAmount() - transaction.getAmountGoal()); // Restar el monto de la meta
            goalRepository.save(goal);
        }
        transactionRepository.delete(transaction);
    }
}
