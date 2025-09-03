package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsuarioUseCaseTest {
    private UsuarioRepository usuarioRepository;
    private UsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        useCase = new UsuarioUseCase(usuarioRepository);
    }

    private Usuario crearUsuarioEjemplo() {
        return Usuario.toUsuario(
                null,
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
    @DisplayName("Crear Usuario")
    class CrearUsuario {

        @Test
        @DisplayName("Debe emitir UsuarioYaExisteException cuando el correo ya existe")
        void debeEmitirExcepcionCuandoCorreoYaExiste() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.just(true));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof AlreadyExistException;
                        AlreadyExistException alreadyExistException = (AlreadyExistException) error;
                        assert alreadyExistException.getMessage().contains("Ya existe un usuario con el email: " + usuario.getEmail());
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe crear usuario exitosamente cuando el correo no existe")
        void debeCrearUsuarioExitosamenteCuandoCorreoNoExiste() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            Long idEsperado = 10L;
            Usuario usuarioConId = Usuario.toUsuario(
                    idEsperado,
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getDocumentoIdentidad(),
                    usuario.getTelefono(),
                    usuario.getIdRol(),
                    usuario.getSalarioBase(),
                    usuario.getContrasenia()
            );

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.just(false));
            when(usuarioRepository.crear(any(Usuario.class)))
                    .thenReturn(Mono.just(usuarioConId));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectNextMatches(usuarioGuardado ->
                        usuarioGuardado.getIdUsuario() != null &&
                        usuarioGuardado.getIdUsuario().equals(idEsperado)
                    )
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe envolver error del repositorio en TechnicalException al verificar email")
        void debeEnvolverErrorDelRepositorioEnTechnicalExceptionAlVerificarEmail() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            RuntimeException errorOriginal = new RuntimeException("Error de conexión");

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.error(errorOriginal));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof TechnicalException;
                        TechnicalException technicalException = (TechnicalException) error;
                        assert technicalException.getMessage().equals("Error al crear el usuario");
                        assert technicalException.getCause().equals(errorOriginal);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe envolver error del repositorio en TechnicalException al crear usuario")
        void debeEnvolverErrorDelRepositorioEnTechnicalExceptionAlCrear() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            RuntimeException errorOriginal = new RuntimeException("Error al guardar");

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.just(false));
            when(usuarioRepository.crear(any(Usuario.class)))
                    .thenReturn(Mono.error(errorOriginal));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof TechnicalException;
                        TechnicalException technicalException = (TechnicalException) error;
                        assert technicalException.getMessage().equals("Error al crear el usuario");
                        assert technicalException.getCause().equals(errorOriginal);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe preservar BusinessException sin envolver")
        void debePreservarBusinessExceptionSinEnvolver() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            ValidationException businessException = new ValidationException("Error de validación");

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.error(businessException));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(ValidationException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("Buscar Usuario por Documento de Identidad")
    class BuscarUsuarioPorDocumentoIdentidad {

        @Test
        @DisplayName("Debe retornar usuario cuando existe con documento válido")
        void debeRetornarUsuarioCuandoExisteConDocumentoValido() {
            // Arrange
            String documentoIdentidad = "CC123";
            Usuario usuarioBase = crearUsuarioEjemplo();
            Usuario usuarioEsperado = Usuario.toUsuario(
                    1L,
                    usuarioBase.getNombre(),
                    usuarioBase.getApellido(),
                    usuarioBase.getEmail(),
                    usuarioBase.getDocumentoIdentidad(),
                    usuarioBase.getTelefono(),
                    usuarioBase.getIdRol(),
                    usuarioBase.getSalarioBase(),
                    usuarioBase.getContrasenia()
            );

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe emitir NotFoundException cuando no existe usuario con el documento")
        void debeEmitirNotFoundExceptionCuandoNoExisteUsuario() {
            // Arrange
            String documentoIdentidad = "CC999";

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.empty());

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof NotFoundException;
                        NotFoundException notFoundException = (NotFoundException) error;
                        assert notFoundException.getMessage().contains("No existe un usuario con el documento de identidad proporcionado: " + documentoIdentidad);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando documento es null")
        void debeEmitirValidationExceptionCuandoDocumentoEsNull() {
            // Arrange
            String documentoNull = null;

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoNull);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El documento de identidad es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando documento está vacío")
        void debeEmitirValidationExceptionCuandoDocumentoEstaVacio() {
            // Arrange
            String documentoVacio = "";

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoVacio);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El documento de identidad es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir ValidationException cuando documento solo tiene espacios")
        void debeEmitirValidationExceptionCuandoDocumentoSoloTieneEspacios() {
            // Arrange
            String documentoConEspacios = "   ";

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoConEspacios);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof ValidationException;
                        ValidationException validationException = (ValidationException) error;
                        assert validationException.getMessage().equals("El documento de identidad es requerido");
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe recortar espacios del documento antes de buscar")
        void debeRecortarEspaciosDelDocumentoAntesDeBuscar() {
            // Arrange
            String documentoConEspacios = "  CC123  ";
            String documentoLimpio = "CC123";
            Usuario usuarioBase = crearUsuarioEjemplo();
            Usuario usuarioEsperado = Usuario.toUsuario(
                    1L,
                    usuarioBase.getNombre(),
                    usuarioBase.getApellido(),
                    usuarioBase.getEmail(),
                    usuarioBase.getDocumentoIdentidad(),
                    usuarioBase.getTelefono(),
                    usuarioBase.getIdRol(),
                    usuarioBase.getSalarioBase(),
                    usuarioBase.getContrasenia()
            );

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoLimpio))
                    .thenReturn(Mono.just(usuarioEsperado));

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoConEspacios);

            // Assert
            StepVerifier.create(resultado)
                    .expectNext(usuarioEsperado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe envolver error del repositorio en TechnicalException")
        void debeEnvolverErrorDelRepositorioEnTechnicalException() {
            // Arrange
            String documentoIdentidad = "CC123";
            RuntimeException errorOriginal = new RuntimeException("Error de conexión");

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.error(errorOriginal));

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof TechnicalException;
                        TechnicalException technicalException = (TechnicalException) error;
                        assert technicalException.getMessage().equals("Error al buscar el usuario por documento");
                        assert technicalException.getCause().equals(errorOriginal);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe preservar BusinessException del repositorio sin envolver")
        void debePreservarBusinessExceptionDelRepositorioSinEnvolver() {
            // Arrange
            String documentoIdentidad = "CC123";
            ValidationException businessException = new ValidationException("Error de validación en repositorio");

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.error(businessException));

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(ValidationException.class)
                    .verify();
        }
    }
}
