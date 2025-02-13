package com.api.finances_backend.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(null)
    public String name;
    @NotBlank
    public String email;
    @NotBlank
    public String password;
}
