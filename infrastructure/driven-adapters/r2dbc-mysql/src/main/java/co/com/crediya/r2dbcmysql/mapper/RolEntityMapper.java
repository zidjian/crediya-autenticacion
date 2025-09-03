package co.com.crediya.r2dbcmysql.mapper;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.r2dbcmysql.entities.RolEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolEntityMapper {
    default RolEntity toEntity(Rol domain) {
        if (domain == null) {
            return null;
        }
        return RolEntity.builder()
                .idRol(domain.getIdRol())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .build();
    }

    default Rol toDomain(RolEntity entity) {
        if (entity == null) {
            return null;
        }
        return Rol.toRol(
                entity.getIdRol(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }
}