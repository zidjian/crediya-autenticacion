package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Mono<Rol> findByIdRol(Long usuario);
}
