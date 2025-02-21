package com.api.finances_backend.services;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private  final TransactionRepository transactionRepository;
    private  final AuthService authService;

    // Obtener todas las transacciones del usuario autenticado
    public List<Transaction> getUserTransactions() {
        User user = authService.getCurrentUser(); // Obtener usuario autenticado
        return transactionRepository.findByUserId(user.getId());
    }

    // Obtener una transacción por ID del usuario autenticado
    public Optional<Transaction> getTransactionById(Long id) {
        User user = authService.getCurrentUser();
        return transactionRepository.findById(id)
                .filter(transaction -> transaction.getUser().getId().equals(user.getId())); // Verificar dueño
    }

    // Crear una nueva transacción para el usuario autenticado
    public Transaction createTransaction(Transaction transaction) {
        User user = authService.getCurrentUser();
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    // Actualizar una transacción si pertenece al usuario autenticado
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        User user = authService.getCurrentUser();
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
        if(!existingTransaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tiene permiso para moodifcar esta transaccion");
        }
        //Actuliza los campos de la transaccion
        if(updatedTransaction.getAmount() != 0){
            existingTransaction.setAmount(updatedTransaction.getAmount());
        }

        if(updatedTransaction.getDescription() != null){
            existingTransaction.setDescription(updatedTransaction.getDescription());
        }

        if(updatedTransaction.getDate() != null){
            existingTransaction.setDate(updatedTransaction.getDate());
        }

        if(updatedTransaction.getPaymentMethod() != null){
            existingTransaction.setPaymentMethod(updatedTransaction.getPaymentMethod());
        }

        if(updatedTransaction.getSourceAccounts() != null){
            existingTransaction.setSourceAccounts(updatedTransaction.getSourceAccounts());
        }

        if(updatedTransaction.getType() != null){
            existingTransaction.setType(updatedTransaction.getType());
        }

        return  transactionRepository.save(existingTransaction);
    }

    // Eliminar una transacción si pertenece al usuario autenticado
    public void deleteTransaction(Long id) {
        User user = authService.getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada o no autorizada"));
        transactionRepository.delete(transaction);
    }
}