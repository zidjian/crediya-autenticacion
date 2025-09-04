package co.com.crediya.model.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    // Constantes para datos de prueba
    private static final Long ID_ROL_VALIDO = 1L;
    private static final String NOMBRE_VALIDO = "ADMIN";
    private static final String DESCRIPCION_VALIDA = "Rol de administrador";

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
                    () -> Rol.toRol(ID_ROL_VALIDO, nombreNull, DESCRIPCION_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar nombre vacío")
        void shouldRejectEmptyNombre() {
            // Arrange
            String nombreVacio = "";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Rol.toRol(ID_ROL_VALIDO, nombreVacio, DESCRIPCION_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar nombre con solo espacios")
        void shouldRejectBlankNombre() {
            // Arrange
            String nombreEspacios = "   ";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Rol.toRol(ID_ROL_VALIDO, nombreEspacios, DESCRIPCION_VALIDA));

            assertEquals("El nombre es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debe permitir descripción null")
        void shouldAllowNullDescripcion() {
            // Arrange
            String descripcionNull = null;

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionNull);

            // Assert
            assertNotNull(rol);
            assertEquals(NOMBRE_VALIDO, rol.getNombre());
            assertNull(rol.getDescripcion()); // La descripción debe permanecer null cuando se pasa null
        }

        @Test
        @DisplayName("Debe permitir ID null")
        void shouldAllowNullId() {
            // Arrange
            Long idNull = null;

            // Act
            Rol rol = Rol.toRol(idNull, NOMBRE_VALIDO, DESCRIPCION_VALIDA);

            // Assert
            assertNotNull(rol);
            assertNull(rol.getIdRol());
        }
    }

    @Nested
    @DisplayName("Funcionalidades")
    class Functionality {

        @Test
        @DisplayName("Debe crear rol exitosamente con todos los campos válidos")
        void debeCrearRolExitosamenteConTodosLosCamposValidos() {
            // Arrange
            Long idRol = 1L;
            String nombre = "ADMIN";
            String descripcion = "Rol de administrador del sistema";

            // Act
            Rol rol = Rol.toRol(idRol, nombre, descripcion);

            // Assert
            assertAll(
                    () -> assertEquals(idRol, rol.getIdRol()),
                    () -> assertEquals(nombre, rol.getNombre()),
                    () -> assertEquals(descripcion, rol.getDescripcion())
            );
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco del nombre")
        void shouldTrimNombre() {
            // Arrange
            String nombreConEspacios = "  ADMIN  ";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreConEspacios, DESCRIPCION_VALIDA);

            // Assert
            assertEquals("ADMIN", rol.getNombre());
        }

        @Test
        @DisplayName("Debe recortar espacios en blanco de la descripción")
        void shouldTrimDescripcion() {
            // Arrange
            String descripcionConEspacios = "  Descripción del rol  ";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionConEspacios);

            // Assert
            assertEquals("Descripción del rol", rol.getDescripcion());
        }

        @Test
        @DisplayName("Debe validar que getters devuelven valores correctos")
        void shouldValidateGettersReturnCorrectValues() {
            // Arrange
            Long idEsperado = 5L;
            String nombreEsperado = "USER";
            String descripcionEsperada = "Usuario básico del sistema";

            // Act
            Rol rol = Rol.toRol(idEsperado, nombreEsperado, descripcionEsperada);

            // Assert
            assertEquals(idEsperado, rol.getIdRol());
            assertEquals(nombreEsperado, rol.getNombre());
            assertEquals(descripcionEsperada, rol.getDescripcion());
        }

        @Test
        @DisplayName("Debe manejar descripción vacía")
        void shouldHandleEmptyDescripcion() {
            // Arrange
            String descripcionVacia = "";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionVacia);

            // Assert
            assertEquals("", rol.getDescripcion());
        }

        @Test
        @DisplayName("Debe manejar descripción con solo espacios")
        void shouldHandleBlankDescripcion() {
            // Arrange
            String descripcionEspacios = "   ";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionEspacios);

            // Assert
            assertEquals("", rol.getDescripcion());
        }
    }

    @Nested
    @DisplayName("Casos límite y edge cases")
    class EdgeCases {

        @Test
        @DisplayName("Debe manejar nombres con caracteres especiales")
        void shouldHandleNombresWithSpecialCharacters() {
            // Arrange
            String nombreEspecial = "SUPER-ADMIN_2024";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreEspecial, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(nombreEspecial, rol.getNombre());
        }

        @Test
        @DisplayName("Debe manejar descripciones con caracteres especiales")
        void shouldHandleDescripcionesWithSpecialCharacters() {
            // Arrange
            String descripcionEspecial = "Rol para administradores con acceso completo (nivel 5) - versión 2.0";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionEspecial);

            // Assert
            assertEquals(descripcionEspecial, rol.getDescripcion());
        }

        @Test
        @DisplayName("Debe manejar IDs grandes")
        void shouldHandleLargeIds() {
            // Arrange
            Long idGrande = Long.MAX_VALUE;

            // Act
            Rol rol = Rol.toRol(idGrande, NOMBRE_VALIDO, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(idGrande, rol.getIdRol());
        }

        @Test
        @DisplayName("Debe manejar nombres de un solo carácter")
        void shouldHandleSingleCharacterNombre() {
            // Arrange
            String nombreCorto = "A";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreCorto, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(nombreCorto, rol.getNombre());
        }

        @Test
        @DisplayName("Debe manejar nombres largos")
        void shouldHandleLongNombres() {
            // Arrange
            String nombreLargo = "SUPER_ADMINISTRATOR_WITH_FULL_ACCESS_AND_SPECIAL_PERMISSIONS";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreLargo, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(nombreLargo, rol.getNombre());
        }

        @Test
        @DisplayName("Debe manejar descripciones largas")
        void shouldHandleLongDescripciones() {
            // Arrange
            String descripcionLarga = "Este es un rol con una descripción muy larga que incluye múltiples responsabilidades, " +
                    "permisos especiales, y características avanzadas para usuarios con privilegios administrativos completos " +
                    "en todo el sistema de gestión empresarial";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, NOMBRE_VALIDO, descripcionLarga);

            // Assert
            assertEquals(descripcionLarga, rol.getDescripcion());
        }

        @Test
        @DisplayName("Debe manejar nombres en minúsculas")
        void shouldHandleLowercaseNombres() {
            // Arrange
            String nombreMinuscula = "admin";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreMinuscula, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(nombreMinuscula, rol.getNombre());
        }

        @Test
        @DisplayName("Debe manejar nombres mixtos")
        void shouldHandleMixedCaseNombres() {
            // Arrange
            String nombreMixto = "SuperAdmin";

            // Act
            Rol rol = Rol.toRol(ID_ROL_VALIDO, nombreMixto, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(nombreMixto, rol.getNombre());
        }

        @Test
        @DisplayName("Debe manejar ID cero")
        void shouldHandleZeroId() {
            // Arrange
            Long idCero = 0L;

            // Act
            Rol rol = Rol.toRol(idCero, NOMBRE_VALIDO, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(idCero, rol.getIdRol());
        }

        @Test
        @DisplayName("Debe manejar ID negativo")
        void shouldHandleNegativeId() {
            // Arrange
            Long idNegativo = -1L;

            // Act
            Rol rol = Rol.toRol(idNegativo, NOMBRE_VALIDO, DESCRIPCION_VALIDA);

            // Assert
            assertEquals(idNegativo, rol.getIdRol());
        }
    }
}
