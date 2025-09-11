package co.com.crediya.api.dto;

import java.math.BigDecimal;

public record RespuestaUsuarioDTO(
        Long idUsuario,
        String nombre,
        String apellido,
        String email,
        String documentoIdentidad,
        String telefono,
        String rol,
        BigDecimal salarioBase
)  {
}
