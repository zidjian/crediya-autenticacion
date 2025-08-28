package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.UsuarioYaExisteException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> crearUsuario(Usuario usuario) {
        return usuarioRepository.existePorEmail(usuario.getEmail())
                .flatMap(existe ->
                        existe ? Mono.error(new UsuarioYaExisteException(usuario.getEmail()))
                                : usuarioRepository.crear(usuario)
                );
    }
}
