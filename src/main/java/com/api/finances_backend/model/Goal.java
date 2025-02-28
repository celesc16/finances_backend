package com.api.finances_backend.model;


import com.api.finances_backend.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description; //Descripcion
    private LocalDate deadLine;   //Fecha limite para alcanzar la meta
    private double currentAmount; //Monto Actual
    private double targetAmount;  //Monto Objetivo

    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @OneToMany(mappedBy = "goal" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference(value = "goal-transactions")
    private List<Transaction> transactions; //Relacion con las transacciones de las metas

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    @JsonBackReference(value = "user-goals")
    private User user; // Relación con el usuarid


    public enum GoalStatus {
        COMPLETED,
        IN_PROGRESS,
        NOT_ACHIEVED
    }


}
