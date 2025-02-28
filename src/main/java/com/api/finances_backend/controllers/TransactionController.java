package com.api.finances_backend.controllers;

import com.api.finances_backend.dtos.TransactionRequest;
import com.api.finances_backend.services.GoalService;
import com.api.finances_backend.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final GoalService goalService;

    // Obtener todas las transacciones del usuario
    @GetMapping
    public List<TransactionRequest> getTransactions() {
        return transactionService.getUserTransactions();
    }

    // Obtener una transacción por ID
    @GetMapping("/{id}")
    public TransactionRequest getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }

    // Crear una nueva transacción
    @PostMapping
    public TransactionRequest createTransaction(@RequestBody TransactionRequest transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    // Actualizar una transacción existente
    @PutMapping("/{id}")
    public TransactionRequest updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    // Eliminar una transacción
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
