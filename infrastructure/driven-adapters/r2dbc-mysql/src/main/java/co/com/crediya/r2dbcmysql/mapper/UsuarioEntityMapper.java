package co.com.crediya.r2dbcmysql.mapper;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.r2dbcmysql.entities.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {
    default UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioEntity.builder()
                .idUsuario(domain.getIdUsuario())
                .nombre(domain.getNombre())
                .apellido(domain.getApellido())
                .email(domain.getEmail())
                .documentoIdentidad(domain.getDocumentoIdentidad())
                .telefono(domain.getTelefono())
                .idRol(domain.getIdRol())
                .salarioBase(domain.getSalarioBase())
                .build();
    }

    default Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        Usuario creado = Usuario.toUsuario(
                entity.getIdUsuario(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getDocumentoIdentidad(),
                entity.getTelefono(),
                entity.getIdRol(),
                entity.getSalarioBase()
        );
        return entity.getIdUsuario() == null ? creado.conId(null) : creado;
    }
}