package com.api.finances_backend.dtos;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String type;
    private double amount;
    private double amountGoal;
    private String category;
    private LocalDate date;
    private String description;
    private String paymentMethod;
    private Boolean recurrent;

    private String frequency;
    private Long goalId;

    // Validación Si se asigna un monto a la meta, debe haber una meta asociada
    @AssertTrue(message = "Debe seleccionar una meta si se asigna un monto a la meta")
    public boolean isAmountGoalValid() {
        return amountGoal == 0 || goalId != null;
    }


}
