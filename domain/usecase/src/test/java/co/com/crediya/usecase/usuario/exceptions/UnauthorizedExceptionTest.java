package co.com.crediya.usecase.usuario.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest {
    @Test
    void constructorSinParametros_deberiaInicializarConValoresPorDefecto() {
        // Arrange & Act
        UnauthorizedException exception = new UnauthorizedException();

        // Assert
        assertEquals("UNAUTHORIZED", exception.getCode());
        assertEquals("Credenciales inválidas", exception.getMessage());
        assertEquals(401, exception.getHttpStatus());
    }

    @Test
    void constructorConMensaje_deberiaInicializarConMensajePersonalizado() {
        // Arrange
        String mensaje = "Acceso denegado";

        // Act
        UnauthorizedException exception = new UnauthorizedException(mensaje);

        // Assert
        assertEquals("UNAUTHORIZED", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(401, exception.getHttpStatus());
    }

    @Test
    void constructorConMensajeYCausa_deberiaInicializarConMensajeYCausa() {
        // Arrange
        String mensaje = "Token inválido";
        Throwable causa = new RuntimeException("Causa interna");

        // Act
        UnauthorizedException exception = new UnauthorizedException(mensaje, causa);

        // Assert
        assertEquals("UNAUTHORIZED", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(401, exception.getHttpStatus());
        assertEquals(causa, exception.getCause());
    }
}