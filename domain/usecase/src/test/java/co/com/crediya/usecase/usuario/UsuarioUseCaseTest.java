package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.UsuarioYaExisteException;
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
                new BigDecimal("1000000")
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
            String emailEsperado = "juan.perez@example.com";

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.just(true));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorSatisfies(error -> {
                        assert error instanceof UsuarioYaExisteException;
                        UsuarioYaExisteException excepcion = (UsuarioYaExisteException) error;
                        assert excepcion.getEmail().equals(emailEsperado);
                    })
                    .verify();
        }

        @Test
        @DisplayName("Debe guardar el usuario cuando el correo no existe")
        void debeGuardarUsuarioCuandoCorreoNoExiste() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            Long idEsperado = 10L;
            Usuario usuarioConId = usuario.conId(idEsperado);

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
        @DisplayName("Debe manejar error del repositorio al verificar existencia del email")
        void debeManejarErrorDelRepositorioAlVerificarEmail() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            RuntimeException errorEsperado = new RuntimeException("Error de base de datos");

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.error(errorEsperado));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar error del repositorio al crear usuario")
        void debeManejarErrorDelRepositorioAlCrear() {
            // Arrange
            Usuario usuario = crearUsuarioEjemplo();
            RuntimeException errorEsperado = new RuntimeException("Error al crear usuario");

            when(usuarioRepository.existePorEmail(usuario.getEmail()))
                    .thenReturn(Mono.just(false));
            when(usuarioRepository.crear(any(Usuario.class)))
                    .thenReturn(Mono.error(errorEsperado));

            // Act
            Mono<Usuario> resultado = useCase.crearUsuario(usuario);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(RuntimeException.class)
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
            Usuario usuarioEsperado = crearUsuarioEjemplo().conId(1L);

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
        @DisplayName("Debe retornar Mono.empty cuando no existe usuario con el documento")
        void debeRetornarMonoEmptyCuandoNoExisteUsuario() {
            // Arrange
            String documentoIdentidad = "CC999";

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.empty());

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe emitir IllegalArgumentException cuando documento es null")
        void debeEmitirExcepcionCuandoDocumentoEsNull() {
            // Arrange
            String documentoNull = null;

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoNull);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                        error.getMessage().equals("El documento de identidad es requerido")
                    )
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir IllegalArgumentException cuando documento está vacío")
        void debeEmitirExcepcionCuandoDocumentoEstaVacio() {
            // Arrange
            String documentoVacio = "";

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoVacio);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                        error.getMessage().equals("El documento de identidad es requerido")
                    )
                    .verify();
        }

        @Test
        @DisplayName("Debe emitir IllegalArgumentException cuando documento solo tiene espacios")
        void debeEmitirExcepcionCuandoDocumentoSoloTieneEspacios() {
            // Arrange
            String documentoConEspacios = "   ";

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoConEspacios);

            // Assert
            StepVerifier.create(resultado)
                    .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                        error.getMessage().equals("El documento de identidad es requerido")
                    )
                    .verify();
        }

        @Test
        @DisplayName("Debe recortar espacios del documento antes de buscar")
        void debeRecortarEspaciosDelDocumentoAntesDeBuscar() {
            // Arrange
            String documentoConEspacios = "  CC123  ";
            String documentoLimpio = "CC123";
            Usuario usuarioEsperado = crearUsuarioEjemplo().conId(1L);

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
        @DisplayName("Debe propagar error del repositorio")
        void debePropagarErrorDelRepositorio() {
            // Arrange
            String documentoIdentidad = "CC123";
            RuntimeException errorEsperado = new RuntimeException("Error de conexión");

            when(usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad))
                    .thenReturn(Mono.error(errorEsperado));

            // Act
            Mono<Usuario> resultado = useCase.buscarUsuarioPorDocumentoIdentidad(documentoIdentidad);

            // Assert
            StepVerifier.create(resultado)
                    .expectError(RuntimeException.class)
                    .verify();
        }
    }
}
