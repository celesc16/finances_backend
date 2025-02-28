package com.api.finances_backend.model;

import com.api.finances_backend.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;   //INGRESO O EGRESO
    private double amount;
    private double amountGoal;
    private String category;
    private LocalDate date;

    private String description;
    private String paymentMethod;

    private Boolean recurrent;
    private String frequency;


    @ManyToOne
    @JoinColumn(name = "goal_id" , nullable = true)
    @JsonBackReference(value = "goal-transactions")
    private Goal goal; // Relación con la metas

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-transactions")
    private User user;

}
