package co.com.crediya.model.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    @Nested
    @DisplayName("Invariantes de negocio")
    class Invariants {

        @Test
        @DisplayName("Debe requerir nombre, apellido, email, salario")
        void shouldRequireMandatoryFields() {
            // Nombre obligatorio
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear(null, "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El nombre es obligatorio", ex1.getMessage());

            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("", "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El nombre es obligatorio", ex2.getMessage());

            IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("   ", "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El nombre es obligatorio", ex3.getMessage());

            // Apellido obligatorio
            IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("Juan", null, "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El apellido es obligatorio", ex4.getMessage());

            IllegalArgumentException ex5 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("Juan", "", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El apellido es obligatorio", ex5.getMessage());

            // Email obligatorio
            IllegalArgumentException ex6 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("Juan", "Perez", null, "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El email es obligatorio", ex6.getMessage());

            IllegalArgumentException ex7 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("Juan", "Perez", "   ", "123456789", "917084202", 1L, BigDecimal.valueOf(1000)));
            assertEquals("El email es obligatorio", ex7.getMessage());

            // Salario obligatorio
            IllegalArgumentException ex8 = assertThrows(IllegalArgumentException.class,
                    () -> Usuario.crear("Juan", "Perez", "test@email.com", "123456789", "917084202", 1L, null));
            assertEquals("El salarioBase es obligatorio", ex8.getMessage());
        }

        @Test
        @DisplayName("Debe permitir campos opcionales como null")
        void shouldAllowOptionalFieldsAsNull() {
            // Teléfono puede ser null
            assertDoesNotThrow(() ->
                    Usuario.crear("Juan", "Perez", "test@email.com", "123456789", null, 1L, BigDecimal.valueOf(1000)));

            // IdRol puede ser null
            assertDoesNotThrow(() ->
                    Usuario.crear("Juan", "Perez", "test@email.com", "123456789", "917084202", null, BigDecimal.valueOf(1000)));

            // Documento de identidad puede ser null
            assertDoesNotThrow(() ->
                    Usuario.crear("Juan", "Perez", "test@email.com", null, "917084202", 1L, BigDecimal.valueOf(1000)));
        }
    }

    @Nested
    @DisplayName("Funcionalidades")
    class Functionality {

        @Test
        @DisplayName("Debe recortar espacios en blanco de los campos de texto")
        void shouldTrimStringFields() {
            Usuario usuario = Usuario.crear("  Juan  ", " Perez ", "  test@email.com  ",
                    "  123456789  ", "  917084202  ", 1L, BigDecimal.valueOf(1000));

            assertEquals("Juan", usuario.getNombre());
            assertEquals("Perez", usuario.getApellido());
            assertEquals("test@email.com", usuario.getEmail());
            assertEquals("123456789", usuario.getDocumentoIdentidad());
            assertEquals("917084202", usuario.getTelefono());
        }

        @Test
        @DisplayName("Debe manejar teléfono null correctamente")
        void shouldHandleNullTelefono() {
            Usuario usuario = Usuario.crear("Juan", "Perez", "test@email.com", "123456789", null, 1L, BigDecimal.valueOf(1000));

            assertNull(usuario.getTelefono());
        }

        @Test
        @DisplayName("Debe crear usuario sin ID inicialmente")
        void shouldCreateUsuarioWithoutId() {
            Usuario usuario = Usuario.crear("Juan", "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000));

            assertNull(usuario.getIdUsuario());
        }

        @Test
        @DisplayName("Debe poder asignar ID usando conId")
        void shouldAssignIdUsingConId() {
            Usuario userSinId = Usuario.crear("Juan", "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000));
            Usuario userConId = userSinId.conId(42L);

            assertNull(userSinId.getIdUsuario());
            assertEquals(42L, userConId.getIdUsuario());

            // Verificar que los demás campos se mantienen
            assertEquals(userSinId.getNombre(), userConId.getNombre());
            assertEquals(userSinId.getApellido(), userConId.getApellido());
            assertEquals(userSinId.getEmail(), userConId.getEmail());
            assertEquals(userSinId.getDocumentoIdentidad(), userConId.getDocumentoIdentidad());
            assertEquals(userSinId.getTelefono(), userConId.getTelefono());
            assertEquals(userSinId.getIdRol(), userConId.getIdRol());
            assertEquals(userSinId.getSalarioBase(), userConId.getSalarioBase());
        }

        @Test
        @DisplayName("Debe mantener inmutabilidad")
        void shouldMaintainImmutability() {
            Usuario original = Usuario.crear("Juan", "Perez", "test@email.com", "123456789", "917084202", 1L, BigDecimal.valueOf(1000));
            Usuario conId = original.conId(42L);

            // Los objetos deben ser diferentes
            assertNotSame(original, conId);

            // El objeto original no debe cambiar
            assertNull(original.getIdUsuario());
            assertEquals(42L, conId.getIdUsuario());
        }
    }

    @Nested
    @DisplayName("Getters")
    class Getters {

        @Test
        @DisplayName("Debe retornar todos los valores correctamente")
        void shouldReturnAllValuesCorrectly() {
            Long idUsuario = 1L;
            String nombre = "Juan";
            String apellido = "Perez";
            String email = "test@email.com";
            String documentoIdentidad = "123456789";
            String telefono = "917084202";
            Long idRol = 2L;
            BigDecimal salarioBase = BigDecimal.valueOf(1500.50);

            Usuario user = Usuario.crear(nombre, apellido, email, documentoIdentidad, telefono, idRol, salarioBase)
                    .conId(idUsuario);

            assertEquals(idUsuario, user.getIdUsuario());
            assertEquals(nombre, user.getNombre());
            assertEquals(apellido, user.getApellido());
            assertEquals(email, user.getEmail());
            assertEquals(documentoIdentidad, user.getDocumentoIdentidad());
            assertEquals(telefono, user.getTelefono());
            assertEquals(idRol, user.getIdRol());
            assertEquals(salarioBase, user.getSalarioBase());
        }
    }
}
