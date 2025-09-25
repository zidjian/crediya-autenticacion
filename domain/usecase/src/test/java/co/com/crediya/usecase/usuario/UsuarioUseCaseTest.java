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
    }
}
