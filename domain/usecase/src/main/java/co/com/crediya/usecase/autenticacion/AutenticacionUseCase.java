package co.com.crediya.usecase.autenticacion;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.exceptions.BusinessException;
import co.com.crediya.usecase.usuario.exceptions.NotFoundException;
import co.com.crediya.usecase.usuario.exceptions.TechnicalException;
import co.com.crediya.usecase.usuario.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> autenticarUsuario(String email, String contrasenia) {
        if (email == null || email.trim().isEmpty()) {
            return Mono.error(new ValidationException("El email es requerido"));
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            return Mono.error(new ValidationException("La contraseña es requerida"));
        }

        return usuarioRepository.buscarPorEmail(email.trim())
                .switchIfEmpty(Mono.error(new NotFoundException("Credenciales inválidas")))
                .onErrorMap(throwable -> {
                    // Si ya es una BusinessException, la dejamos pasar
                    if (throwable instanceof BusinessException) {
                        return throwable;
                    }
                    // Para cualquier otro error, lo envolvemos en una TechnicalException
                    return new TechnicalException("Error al autenticar usuario", throwable);
                });
    }
}
