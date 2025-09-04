package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = false)
public record LoginDTO(
        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String contrasenia
) {
}
