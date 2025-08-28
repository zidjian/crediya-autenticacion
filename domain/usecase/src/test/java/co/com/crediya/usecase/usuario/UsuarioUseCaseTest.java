package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.UsuarioYaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UsuarioUseCaseTest {
    private UsuarioRepository usuarioRepository;
    private UsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        useCase = new UsuarioUseCase(usuarioRepository);
    }

    private Usuario UsuarioEjemplo() {
        return Usuario.crear(
                "Juan",
                "Perez",
                "juan.perez@example.com",
                "CC123",
                "3000000000",
                1L,
                new BigDecimal("1000000")
        );
    }

    @Test
    @DisplayName("Si el correo ya existe debe emitir UserAlreadyExistsException")
    void shouldEmitErrorWhenEmailExists() {
        Usuario usuario = UsuarioEjemplo();
        when(usuarioRepository.existePorEmail(usuario.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.crearUsuario(usuario))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof UsuarioYaExisteException;
                    assert ((UsuarioYaExisteException) ex).getEmail().equals("juan.perez@example.com");
                })
                .verify();
    }

    @Test
    @DisplayName("Si el correo no existe debe guardar el usuario")
    void shouldSaveWhenEmailNotExists() {
        Usuario usuario = UsuarioEjemplo();
        when(usuarioRepository.existePorEmail(usuario.getEmail())).thenReturn(Mono.just(false));
        when(usuarioRepository.crear(any(Usuario.class))).thenAnswer(inv -> Mono.just(((Usuario)inv.getArgument(0)).conId(10L)));

        StepVerifier.create(useCase.crearUsuario(usuario))
                .expectNextMatches(saved -> saved.getIdUsuario() != null && saved.getIdUsuario() == 10L)
                .verifyComplete();
    }
}
