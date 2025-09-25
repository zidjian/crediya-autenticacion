package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> crearUsuario(Usuario usuario) {
        return usuarioRepository.existePorEmail(usuario.getEmail())
                .flatMap(existe ->
                        Boolean.TRUE.equals(existe) ?
                                Mono.error(new AlreadyExistException("Ya existe un usuario con el email: " + usuario.getEmail()))
                                : usuarioRepository.crear(usuario)
                )
                .onErrorMap(throwable -> {
                    // Si ya es una BusinessException, la dejamos pasar
                    if (throwable instanceof BusinessException) {
                        return throwable;
                    }
                    // Para cualquier otro error, lo envolvemos en una TechnicalException
                    return new TechnicalException("Error al crear el usuario", throwable);
                });
    }

    public Mono<Usuario> buscarUsuarioPorId(Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            return Mono.error(new ValidationException("El ID de usuario es requerido y debe ser mayor a 0"));
        }

        return usuarioRepository.buscarPorIdUsuario(idUsuario)
                .switchIfEmpty(Mono.error(new NotFoundException("No existe un usuario con el ID proporcionado: " + idUsuario)))
                .onErrorMap(throwable -> {
                    // Si ya es una BusinessException, la dejamos pasar
                    if (throwable instanceof co.com.crediya.usecase.usuario.exceptions.BusinessException) {
                        return throwable;
                    }
                    // Para cualquier otro error, lo envolvemos en una TechnicalException
                    return new TechnicalException("Error al buscar el usuario por ID", throwable);
                });
    }
}
