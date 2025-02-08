package com.api.finances_backend.repository;

import com.api.finances_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId); //Obtener transaction por usuarios
    List<Transaction> findByCategoryId(Long categoryId); //Obtener transacciones por category
}
