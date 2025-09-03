package co.com.crediya.r2dbcmysql;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.r2dbcmysql.entities.UsuarioEntity;
import co.com.crediya.r2dbcmysql.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbcmysql.mapper.UsuarioEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Mono<Usuario> crear(Usuario usuario) {
        UsuarioEntity entity = usuarioEntityMapper.toEntity(usuario);
        return super.repository.save(entity)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> existePorEmail(String email) {
        return super.repository.existsByEmail( email);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Usuario> buscarPorDocumentoIdentidad(String documentoIdentidad) {
        return super.repository.findByDocumentoIdentidad(documentoIdentidad)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Usuario> buscarPorEmail(String email) {
        return super.repository.findByEmail(email)
                .map(usuarioEntityMapper::toDomain);
    }
}
