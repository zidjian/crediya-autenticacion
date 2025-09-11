package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlreadyExistExceptionTest {

    @Test
    void constructor_asignaCamposCorrectamente() {
        // Arrange
        String mensaje = "El usuario ya existe";

        // Act
        AlreadyExistException exception = new AlreadyExistException(mensaje);

        // Assert
        assertEquals("ALREADY_EXIST", exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(409, exception.getHttpStatus());
    }

    @Test
    void esInstanciaDeBusinessException() {
        // Arrange
        String mensaje = "Ya existe";

        // Act
        AlreadyExistException exception = new AlreadyExistException(mensaje);

        // Assert
        assertInstanceOf(BusinessException.class, exception);
    }
}