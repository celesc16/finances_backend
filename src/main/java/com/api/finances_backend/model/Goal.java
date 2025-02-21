package com.api.finances_backend.model;


import com.api.finances_backend.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate deadLine;   //Fecha limite para alcanzar la meta
    private double currentAmount; //MONTO FINAL
    private double targetAmount;  //Monto OBJETIVO
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    @JsonBackReference
    private User user; // Relación con el usuario

    @OneToMany(mappedBy = "goal" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transaction> transactions; //Relacion con las transacciones de las metas

}
