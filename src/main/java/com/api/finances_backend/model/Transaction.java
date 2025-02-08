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

    private double amount; // Monto de la transacción
    private String description; // Descripción (ej: "Cena en restaurante")
    private LocalDate date; // Fecha de la transacción
    private String type; // Tipo: "INGRESO" o "GASTO"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Relación con el usuario

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Relación con la categoría
}
