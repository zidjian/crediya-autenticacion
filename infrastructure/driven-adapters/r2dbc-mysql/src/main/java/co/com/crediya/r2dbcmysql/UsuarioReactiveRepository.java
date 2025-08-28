package co.com.crediya.r2dbcmysql;

import co.com.crediya.r2dbcmysql.entities.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {
    Mono<Boolean> existsByEmail(String email);
}
