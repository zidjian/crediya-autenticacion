package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NegocioExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con código y mensaje")
    void debeCrearExcepcionConCodigoYMensaje() {
        // Arrange
        String codigo = "TEST_CODE";
        String mensaje = "Mensaje de prueba";

        // Act
        NegocioException exception = new NegocioException(codigo, mensaje);

        // Assert
        assertEquals(codigo, exception.getCode());
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    @DisplayName("Debe manejar código null")
    void debeManejarCodigoNull() {
        // Arrange
        String codigoNull = null;
        String mensaje = "Mensaje de prueba";

        // Act
        NegocioException exception = new NegocioException(codigoNull, mensaje);

        // Assert
        assertNull(exception.getCode());
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    @DisplayName("Debe manejar mensaje null")
    void debeManejarMensajeNull() {
        // Arrange
        String codigo = "TEST_CODE";
        String mensajeNull = null;

        // Act
        NegocioException exception = new NegocioException(codigo, mensajeNull);

        // Assert
        assertEquals(codigo, exception.getCode());
        assertNull(exception.getMessage());
    }
}
