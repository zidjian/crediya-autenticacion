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

    public Mono<Usuario> buscarUsuarioPorDocumentoIdentidad(String documentoIdentidad) {
        if (documentoIdentidad == null || documentoIdentidad.trim().isEmpty()) {
            return Mono.error(new ValidationException("El documento de identidad es requerido"));
        }

        return usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad.trim())
                .switchIfEmpty(Mono.error(new NotFoundException("No existe un usuario con el documento de identidad proporcionado: " + documentoIdentidad)))
                .onErrorMap(throwable -> {
                    // Si ya es una BusinessException, la dejamos pasar
                    if (throwable instanceof co.com.crediya.usecase.usuario.exceptions.BusinessException) {
                        return throwable;
                    }
                    // Para cualquier otro error, lo envolvemos en una TechnicalException
                    return new TechnicalException("Error al buscar el usuario por documento", throwable);
                });
    }
}
