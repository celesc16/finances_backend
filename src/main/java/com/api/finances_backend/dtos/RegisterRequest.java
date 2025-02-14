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
    @NotBlank(message = "El nombre no Puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacio")
    public String name;

    @NotBlank(message = "El correo electrico es obligatorio")
    public String email;

    @NotBlank(message = "La contraseña es obligatoria ")
    public String password;
}
