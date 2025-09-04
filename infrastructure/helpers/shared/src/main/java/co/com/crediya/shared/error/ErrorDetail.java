package co.com.crediya.shared.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String campo;
    private String mensaje;
    private Object valorRechazado;

    // Constructor de dos parámetros para compatibilidad con GlobalErrorHandler
    public ErrorDetail(String campo, String mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }
}
