package co.com.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CrearUsuarioDTO(
        @NotBlank
        String nombre,

        @NotBlank
        String apellido,

        @Email
        @NotBlank
        String email,
        String documentoIdentidad,
        String telefono,
        Long idRol,

        @NotNull
        @Min(1)
        BigDecimal salarioBase
) {
}
