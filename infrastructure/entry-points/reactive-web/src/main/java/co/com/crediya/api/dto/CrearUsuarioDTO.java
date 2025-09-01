package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

@JsonIgnoreProperties(ignoreUnknown = false)
public record CrearUsuarioDTO(
        @NotBlank
        String nombre,

        @NotBlank
        String apellido,

        @Email
        @NotBlank
        String email,
        @NotBlank(message = "El documento de identidad es requerido")
        @Pattern(regexp = "^[0-9-]+$", message = "El documento de identidad solo puede contener números y guiones")
        String documentoIdentidad,
        String telefono,
        Long idRol,

        @NotNull
        @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Solo números, hasta dos decimales")
        @Min(1)
        @Max(value = 15000000, message = "El salario no puede ser mayor a 15.000.000")
        String salarioBase
) {
}
