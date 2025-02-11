package com.api.finances_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate date;
    private double amountInit;
    private double amountFinal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Relación con el usuario

}
