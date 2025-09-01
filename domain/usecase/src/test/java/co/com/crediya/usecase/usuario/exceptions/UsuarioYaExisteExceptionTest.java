package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioYaExisteExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con email y mensaje correcto")
    void debeCrearExcepcionConEmailYMensajeCorrecto() {
        // Arrange
        String email = "test@example.com";
        String mensajeEsperado = "Ya existe un usuario con el correo " + email;
        String codigoEsperado = "USER_ALREADY_EXISTS";

        // Act
        UsuarioYaExisteException exception = new UsuarioYaExisteException(email);

        // Assert
        assertEquals(email, exception.getEmail());
        assertEquals(mensajeEsperado, exception.getMessage());
        assertEquals(codigoEsperado, exception.getCode());
    }

    @Test
    @DisplayName("Debe manejar email null correctamente")
    void debeManejarEmailNullCorrectamente() {
        // Arrange
        String emailNull = null;
        String mensajeEsperado = "Ya existe un usuario con el correo null";
        String codigoEsperado = "USER_ALREADY_EXISTS";

        // Act
        UsuarioYaExisteException exception = new UsuarioYaExisteException(emailNull);

        // Assert
        assertNull(exception.getEmail());
        assertEquals(mensajeEsperado, exception.getMessage());
        assertEquals(codigoEsperado, exception.getCode());
    }

    @Test
    @DisplayName("Debe manejar email vacío correctamente")
    void debeManejarEmailVacioCorrectamente() {
        // Arrange
        String emailVacio = "";
        String mensajeEsperado = "Ya existe un usuario con el correo ";
        String codigoEsperado = "USER_ALREADY_EXISTS";

        // Act
        UsuarioYaExisteException exception = new UsuarioYaExisteException(emailVacio);

        // Assert
        assertEquals(emailVacio, exception.getEmail());
        assertEquals(mensajeEsperado, exception.getMessage());
        assertEquals(codigoEsperado, exception.getCode());
    }

    @Test
    @DisplayName("Debe heredar de NegocioException")
    void debeHeredarDeNegocioException() {
        // Arrange
        String email = "test@example.com";

        // Act
        UsuarioYaExisteException exception = new UsuarioYaExisteException(email);

        // Assert
        assertTrue(exception instanceof NegocioException);
        assertTrue(exception instanceof RuntimeException);
    }
}
