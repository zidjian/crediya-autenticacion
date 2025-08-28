package co.com.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EditarUsuarioDTO(
        @NotNull
        Long idUsuario,
        String nombre,
        String apellido,

        @Email
        String email,
        String documentoIdentidad,
        String telefono,
        Long idRol,

        @Min(1)
        BigDecimal salarioBase
){
}
