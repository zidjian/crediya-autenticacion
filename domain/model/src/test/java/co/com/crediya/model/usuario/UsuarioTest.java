package co.com.crediya.model.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    // Constantes para datos de prueba
    private static final Long ID_USUARIO_VALIDO = 1L;
    private static final String NOMBRE_VALIDO = "Juan";
    private static final String APELLIDO_VALIDO = "Perez";
    private static final String EMAIL_VALIDO = "test@email.com";
    private static final String DOCUMENTO_VALIDO = "123456789";
    private static final String TELEFONO_VALIDO = "917084202";
    private static final Long ID_ROL_VALIDO = 1L;
    private static final BigDecimal SALARIO_VALIDO = BigDecimal.valueOf(1000);
    private static final String CONTRASENIA_VALIDA = "password123";

    @Nested
    @DisplayName("Rompen las reglas de negocio")
    class Invariants {

        @Test
        @DisplayName("Debe rechazar nombre null")
        void shouldRejectNullNombre() {
            // Arrange
            String nombreNull = null;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, nombreNull, APELLIDO_VALIDO, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar nombre vacío")
        void shouldRejectEmptyNombre() {
            // Arrange
            String nombreVacio = "";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, nombreVacio, APELLIDO_VALIDO, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar nombre con solo espacios")
        void shouldRejectBlankNombre() {
            // Arrange
            String nombreEspacios = "   ";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, nombreEspacios, APELLIDO_VALIDO, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar apellido null")
        void shouldRejectNullApellido() {
            // Arrange
            String apellidoNull = null;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, apellidoNull, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El apellido es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar apellido vacío")
        void shouldRejectEmptyApellido() {
            // Arrange
            String apellidoVacio = "";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, apellidoVacio, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El apellido es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar email null")
        void shouldRejectNullEmail() {
            // Arrange
            String emailNull = null;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, emailNull,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El email es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar email con solo espacios")
        void shouldRejectBlankEmail() {
            // Arrange
            String emailEspacios = "   ";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, emailEspacios,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA));

            assertEquals("El email es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar salario null")
        void shouldRejectNullSalario() {
            // Arrange
            BigDecimal salarioNull = null;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                            DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, salarioNull, CONTRASENIA_VALIDA));

            assertEquals("El salarioBase es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe permitir teléfono null")
        void shouldAllowNullTelefono() {
            // Arrange
            String telefonoNull = null;

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, telefonoNull, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertNotNull(usuario);
            assertNull(usuario.getTelefono());
        }

        @Test
        @DisplayName("Debe permitir idRol null")
        void shouldAllowNullIdRol() {
            // Arrange
            Long idRolNull = null;

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, idRolNull, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertNotNull(usuario);
            assertNull(usuario.getIdRol());
        }

        @Test
        @DisplayName("Debe permitir documento de identidad null")
        void shouldAllowNullDocumentoIdentidad() {
            // Arrange
            String documentoNull = null;

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    documentoNull, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertNotNull(usuario);
            assertNull(usuario.getDocumentoIdentidad());
        }
    }

    @Nested
    @DisplayName("Funcionalidades")
    class Functionality {

        @Test
        @DisplayName("Debe recortar espacios en blanco del nombre")
        void shouldTrimNombre() {
            // Arrange
            String nombreConEspacios = "  Juan  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, nombreConEspacios, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("Juan", usuario.getNombre());
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco del apellido")
        void shouldTrimApellido() {
            // Arrange
            String apellidoConEspacios = " Perez ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, apellidoConEspacios, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("Perez", usuario.getApellido());
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco del email")
        void shouldTrimEmail() {
            // Arrange
            String emailConEspacios = "  test@email.com  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, emailConEspacios,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("test@email.com", usuario.getEmail());
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco del documento de identidad")
        void shouldTrimDocumentoIdentidad() {
            // Arrange
            String documentoConEspacios = "  123456789  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    documentoConEspacios, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("123456789", usuario.getDocumentoIdentidad());
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco del teléfono")
        void shouldTrimTelefono() {
            // Arrange
            String telefonoConEspacios = "  917084202  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, telefonoConEspacios, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("917084202", usuario.getTelefono());
        }

        @Test
        @DisplayName("Debe crear usuario válido con todos los campos opcionales null")
        void shouldCreateValidUsuarioWithAllOptionalFieldsNull() {
            // Arrange
            String documentoNull = null;
            String telefonoNull = null;
            Long idRolNull = null;

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    documentoNull, telefonoNull, idRolNull, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertNotNull(usuario);
            assertEquals(NOMBRE_VALIDO, usuario.getNombre());
            assertEquals(APELLIDO_VALIDO, usuario.getApellido());
            assertEquals(EMAIL_VALIDO, usuario.getEmail());
            assertNull(usuario.getDocumentoIdentidad());
            assertNull(usuario.getTelefono());
            assertNull(usuario.getIdRol());
            assertEquals(SALARIO_VALIDO, usuario.getSalarioBase());
        }

        @Test
        @DisplayName("Debe recortar espacios del documento cuando no es null")
        void shouldTrimDocumentoWhenNotNull() {
            // Arrange
            String documentoConEspacios = "  123456789  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    documentoConEspacios, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("123456789", usuario.getDocumentoIdentidad());
        }

        @Test
        @DisplayName("Debe recortar espacios del teléfono cuando no es null")
        void shouldTrimTelefonoWhenNotNull() {
            // Arrange
            String telefonoConEspacios = "  917084202  ";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, telefonoConEspacios, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals("917084202", usuario.getTelefono());
        }

        @Test
        @DisplayName("Debe validar que getters devuelven valores correctos")
        void shouldValidateGettersReturnCorrectValues() {
            // Arrange
            Long idEsperado = 1L;
            String nombreEsperado = "Carlos";
            String apellidoEsperado = "García";
            String emailEsperado = "carlos@example.com";
            String documentoEsperado = "987654321";
            String telefonoEsperado = "3001234567";
            Long idRolEsperado = 2L;
            BigDecimal salarioEsperado = new BigDecimal("2500000");

            // Act
            Usuario usuario = Usuario.toUsuario(idEsperado, nombreEsperado, apellidoEsperado, emailEsperado,
                    documentoEsperado, telefonoEsperado, idRolEsperado, salarioEsperado, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(idEsperado, usuario.getIdUsuario());
            assertEquals(nombreEsperado, usuario.getNombre());
            assertEquals(apellidoEsperado, usuario.getApellido());
            assertEquals(emailEsperado, usuario.getEmail());
            assertEquals(documentoEsperado, usuario.getDocumentoIdentidad());
            assertEquals(telefonoEsperado, usuario.getTelefono());
            assertEquals(idRolEsperado, usuario.getIdRol());
            assertEquals(salarioEsperado, usuario.getSalarioBase());
        }
    }

    @Nested
    @DisplayName("Casos límite y edge cases")
    class EdgeCases {

        @Test
        @DisplayName("Debe manejar nombres con caracteres especiales")
        void shouldHandleNombresWithSpecialCharacters() {
            // Arrange
            String nombreEspecial = "José María";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, nombreEspecial, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(nombreEspecial, usuario.getNombre());
        }

        @Test
        @DisplayName("Debe manejar apellidos con caracteres especiales")
        void shouldHandleApellidosWithSpecialCharacters() {
            // Arrange
            String apellidoEspecial = "Hernández-López";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, apellidoEspecial, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(apellidoEspecial, usuario.getApellido());
        }

        @Test
        @DisplayName("Debe manejar salarios decimales")
        void shouldHandleDecimalSalarios() {
            // Arrange
            BigDecimal salarioDecimal = new BigDecimal("1500000.50");

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, salarioDecimal, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(salarioDecimal, usuario.getSalarioBase());
        }

        @Test
        @DisplayName("Debe manejar salarios cero")
        void shouldHandleZeroSalario() {
            // Arrange
            BigDecimal salarioCero = BigDecimal.ZERO;

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, salarioCero, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(salarioCero, usuario.getSalarioBase());
        }

        @Test
        @DisplayName("Debe manejar IDs grandes")
        void shouldHandleLargeIds() {
            // Arrange
            Long idGrande = Long.MAX_VALUE;
            Usuario usuario = Usuario.toUsuario(idGrande, NOMBRE_VALIDO, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(idGrande, usuario.getIdUsuario());
        }

        @Test
        @DisplayName("Debe manejar emails largos")
        void shouldHandleLongEmails() {
            // Arrange
            String emailLargo = "usuario.con.un.email.muy.largo.para.probar.limites@dominio.muy.largo.com";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, APELLIDO_VALIDO, emailLargo,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(emailLargo, usuario.getEmail());
        }

        @Test
        @DisplayName("Debe manejar nombres de un solo carácter")
        void shouldHandleSingleCharacterNombre() {
            // Arrange
            String nombreCorto = "A";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, nombreCorto, APELLIDO_VALIDO, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(nombreCorto, usuario.getNombre());
        }

        @Test
        @DisplayName("Debe manejar apellidos de un solo carácter")
        void shouldHandleSingleCharacterApellido() {
            // Arrange
            String apellidoCorto = "B";

            // Act
            Usuario usuario = Usuario.toUsuario(ID_USUARIO_VALIDO, NOMBRE_VALIDO, apellidoCorto, EMAIL_VALIDO,
                    DOCUMENTO_VALIDO, TELEFONO_VALIDO, ID_ROL_VALIDO, SALARIO_VALIDO, CONTRASENIA_VALIDA);

            // Assert
            assertEquals(apellidoCorto, usuario.getApellido());
        }
    }
}

