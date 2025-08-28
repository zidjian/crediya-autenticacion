package co.com.crediya.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BuscarPorDocumentoDTO(
        @NotBlank(message = "El documento de identidad es requerido")
        @Pattern(regexp = "^[0-9-]+$", message = "El documento de identidad solo puede contener números y guiones")
        String documentoIdentidad
) {
}
