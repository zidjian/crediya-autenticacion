package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnicalExceptionTest {

    @Test
    void deberiaCrearTechnicalExceptionConMensaje() {
        // Arrange
        String mensaje = "Error técnico";

        // Act
        TechnicalException exception = new TechnicalException(mensaje);

        // Assert
        assertEquals("INTERNAL_SERVER_ERROR", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(500, exception.getHttpStatus());
        assertNull(exception.getCause());
    }

    @Test
    void deberiaCrearTechnicalExceptionConMensajeYCausa() {
        // Arrange
        String mensaje = "Error técnico con causa";
        Throwable causa = new RuntimeException("Causa interna");

        // Act
        TechnicalException exception = new TechnicalException(mensaje, causa);

        // Assert
        assertEquals("INTERNAL_SERVER_ERROR", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(500, exception.getHttpStatus());
        assertEquals(causa, exception.getCause());
    }
}