package com.api.finances_backend.services;

import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private  final TransactionRepository transactionRepository;

    //Obtener todas las transacciones
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    //Obtener una transaccion por id
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));
    }

    //Crear una Transaccion
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    //Actualizar Transacciones
    public Transaction updateTransaction(Long id , Transaction transaction) {
        Transaction existingTransaction = getTransactionById(id);
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
        transactionRepository.deleteById(id);
    }

}
