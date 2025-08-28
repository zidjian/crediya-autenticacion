package co.com.crediya.r2dbcmysql;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.r2dbcmysql.entities.UsuarioEntity;
import co.com.crediya.r2dbcmysql.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbcmysql.mapper.UsuarioEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Long,
        UsuarioReactiveRepository
> implements UsuarioRepository {

    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper, UsuarioEntityMapper usuarioEntityMapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    public Mono<Usuario> crear(Usuario usuario) {
        UsuarioEntity entity = mapper.map(usuario, UsuarioEntity.class);
        return super.repository.save(entity)
                .map(data -> mapper.map(data, Usuario.class));
    }

    @Override
    public Mono<Boolean> existePorEmail(String email) {
        return super.repository.existsByEmail( email);
    }
}
