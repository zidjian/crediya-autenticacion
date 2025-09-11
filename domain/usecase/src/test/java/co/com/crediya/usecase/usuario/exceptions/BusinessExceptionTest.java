package co.com.crediya.usecase.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void deberiaCrearBusinessExceptionConParametrosBasicos() {
        // Arrange
        String codigo = "ERR001";
        String mensaje = "Error de negocio";
        int httpStatus = 400;

        // Act
        BusinessException exception = new BusinessException(codigo, mensaje, httpStatus);

        // Assert
        assertEquals(codigo, exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatus());
        assertNull(exception.getCause());
    }

    @Test
    void deberiaCrearBusinessExceptionConCausa() {
        // Arrange
        String codigo = "ERR002";
        String mensaje = "Error con causa";
        int httpStatus = 500;
        Throwable causa = new RuntimeException("Causa interna");

        // Act
        BusinessException exception = new BusinessException(codigo, mensaje, httpStatus, causa);

        // Assert
        assertEquals(codigo, exception.getCode());
        assertEquals(mensaje, exception.getMessage());
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(causa, exception.getCause());
    }
}