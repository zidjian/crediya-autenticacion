package co.com.crediya.r2dbcmysql;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.r2dbcmysql.entities.RolEntity;
import co.com.crediya.r2dbcmysql.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbcmysql.mapper.RolEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<Rol, RolEntity, Long, RolReactiveRepository> implements RolRepository {

    private final RolEntityMapper rolEntityMapper;

    public RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper, RolEntityMapper rolEntityMapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));
        this.rolEntityMapper = rolEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Rol> findByIdRol(Long usuario) {
        return super.repository.findByIdRol(usuario).map(rolEntityMapper::toDomain);
    }
}