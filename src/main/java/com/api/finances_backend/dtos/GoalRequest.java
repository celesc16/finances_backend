package com.api.finances_backend.dtos;

import com.api.finances_backend.model.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalRequest {
    private Long id;

    private String description; //Descripcion
    private LocalDate deadLine;   //Fecha limite para alcanzar la meta
    private double currentAmount; //Monto Actual
    private double targetAmount;  //Monto Objetivo
    private Goal.GoalStatus status;

}
