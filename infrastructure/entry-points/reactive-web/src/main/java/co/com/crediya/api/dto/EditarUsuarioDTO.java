package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EditarUsuarioDTO(
        @NotNull
        Long idUsuario,
        String nombre,
        String apellido,

        @Email
        String email,
        @NotBlank(message = "El documento de identidad es requerido")
        @Pattern(regexp = "^[0-9-]+$", message = "El documento de identidad solo puede contener números y guiones")
        String documentoIdentidad,
        String telefono,
        Long idRol,

        @Min(1)
        BigDecimal salarioBase
){
}
