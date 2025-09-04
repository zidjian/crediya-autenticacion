package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.usecase.usuario.exceptions.BusinessException;
import co.com.crediya.usecase.usuario.exceptions.NotFoundException;
import co.com.crediya.usecase.usuario.exceptions.TechnicalException;
import co.com.crediya.usecase.usuario.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class RolUseCaseTest {

    private RolRepository rolRepository;
    private RolUseCase useCase;

    @BeforeEach
    void setUp() {
        rolRepository = Mockito.mock(RolRepository.class);
        useCase = new RolUseCase(rolRepository);
    }

    private Rol crearRolEjemplo() {
        return Rol.toRol(1L, "ADMIN", "Rol de administrador del sistema");
    }

    @Nested
    @DisplayName("Buscar Rol por ID")
    class FindByIdRol {

        @Test
        @DisplayName("Debe retornar rol exitosamente cuando existe con ID válido")
        void debeRetornarRolExitosamenteCuandoExisteConIdValido() {
            // Arrange
            Long idRol = 1L;
            Rol rolEsperado = crearRolEjemplo();

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.just(rolEsperado));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe emitir NotFoundException cuando no existe rol con el ID")
        void debeEmitirNotFoundExceptionCuandoNoExisteRol() {
            // Arrange
            Long idRol = 999L;

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.empty());

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof NotFoundException;
                        NotFoundException notFoundException = (NotFoundException) error;
                        assert notFoundException.getMessage().equals("Rol con id " + idRol + " no encontrado");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el ID es null")
        void debeEmitirValidationExceptionCuandoIdEsNull() {
            // Arrange
            Long idNull = null;

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idNull);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El ID del rol no puede ser nulo");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el ID es cero")
        void debeEmitirValidationExceptionCuandoIdEsCero() {
            // Arrange
            Long idCero = 0L;

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idCero);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El ID del rol debe ser un número positivo");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el ID es negativo")
        void debeEmitirValidationExceptionCuandoIdEsNegativo() {
            // Arrange
            Long idNegativo = -1L;

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idNegativo);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El ID del rol debe ser un número positivo");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar IDs muy grandes")
        void debeManejarIdsMuyGrandes() {
            // Arrange
            Long idGrande = Long.MAX_VALUE;
            Rol rolEsperado = Rol.toRol(idGrande, "SUPER_ADMIN", "Rol de super administrador");

            when(rolRepository.findByIdRol(idGrande))
                    .thenReturn(Mono.just(rolEsperado));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idGrande);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe envolver error del repositorio en TechnicalException")
        void debeEnvolverErrorDelRepositorioEnTechnicalException() {
            // Arrange
            Long idRol = 1L;
            RuntimeException errorOriginal = new RuntimeException("Error de conexión a la base de datos");

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.error(errorOriginal));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof TechnicalException;
                        TechnicalException technicalException = (TechnicalException) error;
                        assert technicalException.getMessage().equals("Error al buscar el rol con id " + idRol);
                        assert technicalException.getCause().equals(errorOriginal);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe preservar BusinessException del repositorio sin envolver")
        void debePreservarBusinessExceptionDelRepositorioSinEnvolver() {
            // Arrange
            Long idRol = 1L;
            ValidationException businessException = new ValidationException("Error de validación en repositorio");

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.error(businessException));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(ValidationException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe preservar NotFoundException del repositorio sin envolver")
        void debePreservarNotFoundExceptionDelRepositorioSinEnvolver() {
            // Arrange
            Long idRol = 1L;
            NotFoundException notFoundException = new NotFoundException("Rol no encontrado en repositorio");

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.error(notFoundException));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(NotFoundException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar diferentes tipos de roles")
        void debeManejarDiferentesTiposDeRoles() {
            // Arrange
            Long idRol = 2L;
            Rol rolUsuario = Rol.toRol(idRol, "USER", "Rol de usuario básico");

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.just(rolUsuario));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectNextMatches(rol ->
                            rol.getIdRol().equals(idRol) &&
                            rol.getNombre().equals("USER") &&
                            rol.getDescripcion().equals("Rol de usuario básico")
                    )
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe manejar rol con descripción null")
        void debeManejarRolConDescripcionNull() {
            // Arrange
            Long idRol = 3L;
            Rol rolSinDescripcion = Rol.toRol(idRol, "MODERATOR", null);

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.just(rolSinDescripcion));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolSinDescripcion)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe manejar rol con nombre con caracteres especiales")
        void debeManejarRolConNombreConCaracteresEspeciales() {
            // Arrange
            Long idRol = 4L;
            Rol rolEspecial = Rol.toRol(idRol, "SUPER-ADMIN_2024", "Rol de super administrador versión 2024");

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.just(rolEspecial));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolEspecial)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe manejar rol con descripción larga")
        void debeManejarRolConDescripcionLarga() {
            // Arrange
            Long idRol = 5L;
            String descripcionLarga = "Este es un rol con una descripción muy larga que incluye múltiples responsabilidades, " +
                    "permisos especiales, y características avanzadas para usuarios con privilegios administrativos completos " +
                    "en todo el sistema de gestión empresarial";
            Rol rolDescripcionLarga = Rol.toRol(idRol, "ENTERPRISE_ADMIN", descripcionLarga);

            when(rolRepository.findByIdRol(idRol))
                    .thenReturn(Mono.just(rolDescripcionLarga));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idRol);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolDescripcionLarga)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe validar ID mínimo positivo")
        void debeValidarIdMinimoPositivo() {
            // Arrange
            Long idMinimo = 1L;
            Rol rolMinimo = Rol.toRol(idMinimo, "A", "Descripción mínima");

            when(rolRepository.findByIdRol(idMinimo))
                    .thenReturn(Mono.just(rolMinimo));

            // Act
            Mono<Rol> resultado = useCase.findByIdRol(idMinimo);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(rolMinimo)
                    .verifyComplete();
        }
    }
}
