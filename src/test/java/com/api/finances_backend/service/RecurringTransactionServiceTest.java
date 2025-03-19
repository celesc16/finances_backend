package com.api.finances_backend.service;

import com.api.finances_backend.model.Transaction;
import com.api.finances_backend.repository.TransactionRepository;
import com.api.finances_backend.services.RecurringTransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RecurringTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RecurringTransactionService recurringTransactionService;

    @Test
    public void testProcessRecurringTransaction_Monthly() {
        // Configurar datos de pruebas
        Transaction transaction = new Transaction();
        transaction.setId(5L);
        transaction.setType("Ingreso");
        transaction.setAmount(1000);
        transaction.setFrequency("mensual");
        transaction.setDate(LocalDate.now().minusMonths(2)); // hace 2 mes
        transaction.setRecurrent(true);

        // Simulación de repositorio
        when(transactionRepository.findByRecurrentTrue()).thenReturn(List.of(transaction));

        // Llamar al método manualmente
        recurringTransactionService.processRecurringTransactions();

        // Verificar que se guardó una nueva transacción
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

}
