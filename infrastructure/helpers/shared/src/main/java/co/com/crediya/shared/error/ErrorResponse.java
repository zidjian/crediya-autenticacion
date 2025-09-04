package co.com.crediya.shared.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String codigo;
    private String mensaje;
    private int status;
    private String path;
    private List<ErrorDetail> detalles;
    private String traceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Método estático para mantener compatibilidad con tu GlobalErrorHandler
    public static ErrorResponse of(String codigo, String mensaje, int status, String path,
                                 List<ErrorDetail> detalles, String traceId) {
        return ErrorResponse.builder()
                .codigo(codigo)
                .mensaje(mensaje)
                .status(status)
                .path(path)
                .detalles(detalles)
                .traceId(traceId)
                .build();
    }
}
