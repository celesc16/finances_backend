package com.api.finances_backend.services;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.TransactionRepository;
import com.api.finances_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final TransactionRepository transactionRepository;

    // Ejecutar todos los días a medianoche (00:00)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();  // Fecha actual

        // Obtener todas las transacciones recurrentes
        List<Transaction> recurringTransactions = transactionRepository.findByRecurrentTrue();

        for (Transaction transaction : recurringTransactions) {
            try {
                if (shouldCreateRecurringTransaction(transaction, today)) {
                    System.out.println("Creando nueva transacción recurrente para: " + transaction.getId());

                    Transaction newTransaction = new Transaction();
                    newTransaction.setType(transaction.getType());
                    newTransaction.setAmount(transaction.getAmount());
                    newTransaction.setAmountGoal(transaction.getAmountGoal());
                    newTransaction.setCategory(transaction.getCategory());
                    newTransaction.setDate(today); // Actualizar la fecha
                    newTransaction.setDescription(transaction.getDescription());
                    newTransaction.setPaymentMethod(transaction.getPaymentMethod());
                    newTransaction.setRecurrent(transaction.getRecurrent());
                    newTransaction.setFrequency(transaction.getFrequency());
                    newTransaction.setGoal(transaction.getGoal());
                    newTransaction.setUser(transaction.getUser());

                    transaction.setDate(today); // Actualizar la fecha de la transacción original
                    transactionRepository.save(newTransaction); // Guardar la nueva transacción
                    transactionRepository.save(transaction); // Guardar la transacción original actualizada
                } else {
                    System.out.println("No se cumple la condición para crear una nueva transacción: " + transaction.getId());
                }
            } catch (Exception e) {
                // Loggear el error y continuar con la siguiente transacción
                System.err.println("Error procesando transacción recurrente: " + e.getMessage());
            }
        }
    }

    // Método que verifica si se debe crear una nueva transacción recurrente
    public Boolean shouldCreateRecurringTransaction(Transaction transaction, LocalDate today) {
        LocalDate lastTransactionDate = transaction.getDate();

        switch (transaction.getFrequency().toLowerCase()) {
            case "diario":
                return lastTransactionDate.isBefore(today.minusDays(1));
            case "mensual":
                return lastTransactionDate.isBefore(today.minusMonths(1));
            case "trimestral":
                return lastTransactionDate.isBefore(today.minusMonths(3));
            case "anual":
                return lastTransactionDate.isBefore(today.minusYears(1));
            default:
                throw new IllegalArgumentException("Frecuencia no válida: " + transaction.getFrequency());
        }
    }
}