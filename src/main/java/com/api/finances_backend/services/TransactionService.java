package com.api.finances_backend.services;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private  final TransactionRepository transactionRepository;
    private  final AuthService authService;

    //Obtener todas las transacciones del Usuario autenticado
    public List<Transaction> getAllTransactions() {
        Long userId  = authService.getCurrentUserId();
        return transactionRepository.findByUserId(userId);
    }

    //Obtener una transaccion por id
    public Transaction getTransactionById(Long id) {
        Long userId = authService.getCurrentUserId();
        return transactionRepository.findByIdAndUserId(id , userId)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
    }

    //Crear una Transaccion
    public Transaction createTransaction(Transaction transaction) {
        User user = authService.getCurrentUser();
        return transactionRepository.save(transaction);
    }

    //Actualizar Transacciones
    public Transaction updateTransaction(Long id , Transaction transaction) {
        Long userId = authService.getCurrentUserId();
        Transaction existingTransaction = transactionRepository.findByIdAndUserId(id ,userId)
                        .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setType(transaction.getType());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setDescription(transaction.getDescription());
        return transactionRepository.save(existingTransaction);
    }

    //Eliminar Transaccion
    public void deleteTransaction(Long id) {
        Long userId = authService.getCurrentUserId();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
        transactionRepository.delete(transaction);
    }

}
