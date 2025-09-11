package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void deberiaCrearNotFoundExceptionPorDefecto() {
        // Arrange
        // No se requieren parámetros

        // Act
        NotFoundException exception = new NotFoundException();

        // Assert
        assertEquals("NOT_FOUND", exception.getCode());
        assertEquals("No encontrado", exception.getMessage());
        assertEquals(404, exception.getHttpStatus());
        assertNull(exception.getCause());
    }

    @Test
    void deberiaCrearNotFoundExceptionConMensajePersonalizado() {
        // Arrange
        String mensaje = "Usuario no encontrado";

        // Act
        NotFoundException exception = new NotFoundException(mensaje);

        // Assert
        assertEquals("NOT_FOUND", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(404, exception.getHttpStatus());
        assertNull(exception.getCause());
    }

    @Test
    void deberiaCrearNotFoundExceptionConMensajeYCausa() {
        // Arrange
        String mensaje = "Recurso no encontrado";
        Throwable causa = new RuntimeException("Causa interna");

        // Act
        NotFoundException exception = new NotFoundException(mensaje, causa);

        // Assert
        assertEquals("NOT_FOUND", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(404, exception.getHttpStatus());
        assertEquals(causa, exception.getCause());
    }
}