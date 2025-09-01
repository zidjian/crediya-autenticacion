package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioNotFoundExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con mensaje por defecto")
    void debeCrearExcepcionConMensajePorDefecto() {
        // Arrange
        String mensajeEsperado = "Usuario no encontrado";

        // Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException();

        // Assert
        assertEquals(mensajeEsperado, exception.getMessage());
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje personalizado")
    void debeCrearExcepcionConMensajePersonalizado() {
        // Arrange
        String mensajePersonalizado = "Usuario con ID 123 no encontrado";

        // Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException(mensajePersonalizado);

        // Assert
        assertEquals(mensajePersonalizado, exception.getMessage());
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje y causa")
    void debeCrearExcepcionConMensajeYCausa() {
        // Arrange
        String mensaje = "Error al buscar usuario";
        RuntimeException causa = new RuntimeException("Error de conexión");

        // Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException(mensaje, causa);

        // Assert
        assertEquals(mensaje, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Debe manejar mensaje null en constructor con mensaje")
    void debeManejarMensajeNullEnConstructorConMensaje() {
        // Arrange
        String mensajeNull = null;

        // Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException(mensajeNull);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Debe manejar causa null en constructor con mensaje y causa")
    void debeManejarCausaNullEnConstructorConMensajeYCausa() {
        // Arrange
        String mensaje = "Error al buscar usuario";
        Throwable causaNull = null;

        // Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException(mensaje, causaNull);

        // Assert
        assertEquals(mensaje, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debe heredar de RuntimeException")
    void debeHeredarDeRuntimeException() {
        // Arrange & Act
        UsuarioNotFoundException exception = new UsuarioNotFoundException();

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
