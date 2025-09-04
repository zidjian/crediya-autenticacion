package co.com.crediya.usecase.autenticacion;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
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

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

class AutenticacionUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private AutenticacionUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        useCase = new AutenticacionUseCase(usuarioRepository);
    }

    private Usuario crearUsuarioEjemplo() {
        return Usuario.toUsuario(
                1L,
                "Juan",
                "Perez",
                "juan.perez@example.com",
                "CC123",
                "3000000000",
                1L,
                new BigDecimal("1000000"),
                "password123"
        );
    }

    @Nested
    @DisplayName("Autenticar Usuario")
    class AutenticarUsuario {

        @Test
        @DisplayName("Debe autenticar usuario exitosamente con credenciales válidas")
        void debeAutenticarUsuarioExitosamenteConCredencialesValidas() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenia = "password123";
            Usuario usuarioEsperado = crearUsuarioEjemplo();

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe recortar espacios del email antes de buscar")
        void debeRecortarEspaciosDelEmailAntesDeBuscar() {
            // Arrange
            String emailConEspacios = "  juan.perez@example.com  ";
            String emailLimpio = "juan.perez@example.com";
            String contrasenia = "password123";
            Usuario usuarioEsperado = crearUsuarioEjemplo();

            when(usuarioRepository.buscarPorEmail(emailLimpio))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(emailConEspacios, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe emitir NotFoundException cuando el usuario no existe")
        void debeEmitirNotFoundExceptionCuandoUsuarioNoExiste() {
            // Arrange
            String email = "noexiste@example.com";
            String contrasenia = "password123";

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.empty());

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof NotFoundException;
                        NotFoundException notFoundException = (NotFoundException) error;
                        assert notFoundException.getMessage().equals("Credenciales inválidas");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el email es null")
        void debeEmitirValidationExceptionCuandoEmailEsNull() {
            // Arrange
            String emailNull = null;
            String contrasenia = "password123";

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(emailNull, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El email es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el email está vacío")
        void debeEmitirValidationExceptionCuandoEmailEstaVacio() {
            // Arrange
            String emailVacio = "";
            String contrasenia = "password123";

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(emailVacio, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El email es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando el email solo tiene espacios")
        void debeEmitirValidationExceptionCuandoEmailSoloTieneEspacios() {
            // Arrange
            String emailConEspacios = "   ";
            String contrasenia = "password123";

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(emailConEspacios, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El email es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando la contraseña es null")
        void debeEmitirValidationExceptionCuandoContrasenaEsNull() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenaNull = null;

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenaNull);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("La contraseña es requerida");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando la contraseña está vacía")
        void debeEmitirValidationExceptionCuandoContrasenaEstaVacia() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenaVacia = "";

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenaVacia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("La contraseña es requerida");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando la contraseña solo tiene espacios")
        void debeEmitirValidationExceptionCuandoContrasenaSoloTieneEspacios() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenaConEspacios = "   ";

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenaConEspacios);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("La contraseña es requerida");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe envolver error del repositorio en TechnicalException")
        void debeEnvolverErrorDelRepositorioEnTechnicalException() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenia = "password123";
            RuntimeException errorOriginal = new RuntimeException("Error de conexión");

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.error(errorOriginal));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof TechnicalException;
                        TechnicalException technicalException = (TechnicalException) error;
                        assert technicalException.getMessage().equals("Error al autenticar usuario");
                        assert technicalException.getCause().equals(errorOriginal);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe preservar BusinessException del repositorio sin envolver")
        void debePreservarBusinessExceptionDelRepositorioSinEnvolver() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenia = "password123";
            ValidationException businessException = new ValidationException("Error de validación en repositorio");

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.error(businessException));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(ValidationException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar NotFoundException del repositorio sin envolver")
        void debeManejarNotFoundExceptionDelRepositorioSinEnvolver() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenia = "password123";
            NotFoundException notFoundException = new NotFoundException("Usuario no encontrado");

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.error(notFoundException));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(NotFoundException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar emails con caracteres especiales")
        void debeManejarEmailsConCaracteresEspeciales() {
            // Arrange
            String emailEspecial = "juan.perez+test@example.com";
            String contrasenia = "password123";
            Usuario usuarioEsperado = Usuario.toUsuario(
                    1L,
                    "Juan",
                    "Perez",
                    emailEspecial,
                    "CC123",
                    "3000000000",
                    1L,
                    new BigDecimal("1000000"),
                    "password123"
            );

            when(usuarioRepository.buscarPorEmail(emailEspecial))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(emailEspecial, contrasenia);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe manejar contraseñas con caracteres especiales")
        void debeManejarContrasenasConCaracteresEspeciales() {
            // Arrange
            String email = "juan.perez@example.com";
            String contrasenaEspecial = "P@ssw0rd!2024#";
            Usuario usuarioEsperado = crearUsuarioEjemplo();

            when(usuarioRepository.buscarPorEmail(email))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.autenticarUsuario(email, contrasenaEspecial);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }
    }
}
