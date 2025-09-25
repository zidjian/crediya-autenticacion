package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void deberiaCrearValidationExceptionConMensaje() {
        // Arrange
        String mensaje = "Error de validación";

        // Act
        ValidationException exception = new ValidationException(mensaje);

        // Assert
        assertEquals("VALIDATION_ERROR", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
        assertNull(exception.getCause());
    }

    @Test
    void deberiaCrearValidationExceptionConMensajeYCausa() {
        // Arrange
        String mensaje = "Error de validación con causa";
        Throwable causa = new RuntimeException("Causa interna");

        // Act
        ValidationException exception = new ValidationException(mensaje, causa);

        // Assert
        assertEquals("VALIDATION_ERROR", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(400, exception.getHttpStatus());
        assertEquals(causa, exception.getCause());
    }
}