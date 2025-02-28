package com.api.finances_backend.services;

import com.api.finances_backend.dtos.TransactionRequest;
import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.GoalRepository;
import com.api.finances_backend.repository.TransactionRepository;
import com.api.finances_backend.repository.UserRepository;
import com.api.finances_backend.services.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    //Ejecutar todos los dias medianoche (00:00)
    @Scheduled(cron = "0 0 0 * * ?")
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();  //Fecha actual

        //Obtener todas las transacciones recurrentes
        List<Transaction> recurringTransactions = transactionRepository.findByRecurretTrue();

        for(Transaction transaction : recurringTransactions){
            //Verificar si es necesarrio crear una naueva transaccion recurrente

            if(shouldCreateRecurringTransaction(transaction today)){
                TransactionRequest  transactionDTO = transactionMapper.toDto(transaction);
                newTransaction.setType(transaction.getType());
                newTransaction.setDescription(transaction.getDescription());
                newTransaction.setAmount(transaction.getAmount());
                newTransaction.setPaymentMethod(transaction.getPaymentMethod());
                newTransaction.setCategory(transaction.getCategory());
                newTransaction.setGoal(transaction.getGoal());
                newTransaction.setAmountGoal(transaction.getAmountGoal());
            }
        }
    }
}
