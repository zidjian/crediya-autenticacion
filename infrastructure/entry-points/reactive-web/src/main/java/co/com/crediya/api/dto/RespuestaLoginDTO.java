package co.com.crediya.api.dto;

public record RespuestaLoginDTO(
        Long idUsuario,
        String nombre,
        String apellido,
        String email,
        String documentoIdentidad,
        String token
) {
}
