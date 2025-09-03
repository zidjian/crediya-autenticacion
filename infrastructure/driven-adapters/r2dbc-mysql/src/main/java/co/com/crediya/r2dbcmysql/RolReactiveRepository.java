package co.com.crediya.r2dbcmysql;

import co.com.crediya.r2dbcmysql.entities.RolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Long>, ReactiveQueryByExampleExecutor<RolEntity> {
    Mono<RolEntity> findByIdRol(Long idRol);
}
