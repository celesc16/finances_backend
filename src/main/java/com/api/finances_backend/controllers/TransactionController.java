package com.api.finances_backend.controllers;

import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.services.GoalService;
import com.api.finances_backend.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;
    private final GoalService goalService;

    //Obtenie todas las transactions
    @GetMapping
    public List<Transaction> getTransactions() {
        return transactionService.getUserTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }

    //Crea transacciones
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        if(transaction.getGoal() != null) {
            goalService.updateGoalProgress(transaction.getGoal().getId());
        }
        return transactionService.createTransaction(transaction);
    }

    //Actualiza transacciones
    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id , @RequestBody Transaction transaction){
        return transactionService.updateTransaction(id, transaction);
    }

    //Elimina transacciones
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

}
