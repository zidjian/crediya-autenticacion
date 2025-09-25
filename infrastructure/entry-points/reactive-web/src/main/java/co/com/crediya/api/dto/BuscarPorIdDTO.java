package co.com.crediya.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuscarPorIdDTO(
        @NotNull(message = "El ID de usuario es requerido")
        @Positive(message = "El ID de usuario debe ser un número positivo")
        Long idUsuario
) {
}