package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.dto.RespuestaUsuarioDTO;
import co.com.crediya.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {
    default Usuario toModel(CrearUsuarioDTO dto) {
        if (dto == null) return null;
        return Usuario.crear(
                dto.nombre(),
                dto.apellido(),
                dto.email(),
                dto.documentoIdentidad(),
                dto.telefono(),
                dto.idRol(),
                dto.salarioBase()
        );
    }

    @Mappings({
            @Mapping(target = "idUsuario", source = "idUsuario"),
            @Mapping(target = "nombre", source = "nombre"),
            @Mapping(target = "apellido", source = "apellido"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "documentoIdentidad", source = "documentoIdentidad"),
            @Mapping(target = "telefono", source = "telefono"),
            @Mapping(target = "idRol", source = "idRol"),
            @Mapping(target = "salarioBase", source = "salarioBase")
    })
    RespuestaUsuarioDTO toResponse(Usuario usuario);
}
