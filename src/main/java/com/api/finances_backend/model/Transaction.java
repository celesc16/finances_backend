package com.api.finances_backend.model;

import com.api.finances_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDate date;
    private String type;
    private String description;
    private String paymentMethod;
    private String sourceAccounts;


    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user; // Relación con el usuario


    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal; // Relación con la metas


}
