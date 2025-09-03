package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.usecase.usuario.exceptions.BusinessException;
import co.com.crediya.usecase.usuario.exceptions.NotFoundException;
import co.com.crediya.usecase.usuario.exceptions.TechnicalException;
import co.com.crediya.usecase.usuario.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RolUseCase {
    private final RolRepository repository;

    public Mono<Rol> findByIdRol(Long idRol) {
        // Validar que el ID no sea nulo
        if (idRol == null) {
            return Mono.error(new ValidationException("El ID del rol no puede ser nulo"));
        }

        // Validar que el ID sea positivo
        if (idRol <= 0) {
            return Mono.error(new ValidationException("El ID del rol debe ser un número positivo"));
        }

        return repository.findByIdRol(idRol)
                .switchIfEmpty(Mono.error(new NotFoundException("Rol con id " + idRol + " no encontrado")))
                .onErrorMap(throwable -> {
                    // Si ya es una BusinessException, la dejamos pasar
                    if (throwable instanceof BusinessException) {
                        return throwable;
                    }
                    // Para cualquier otro error, lo envolvemos en una TechnicalException
                    return new TechnicalException("Error al buscar el rol con id " + idRol, throwable);
                });
    }
}
