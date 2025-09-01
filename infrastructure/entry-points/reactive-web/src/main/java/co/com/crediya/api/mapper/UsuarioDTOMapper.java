package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.dto.RespuestaUsuarioDTO;
import co.com.crediya.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {
    @ObjectFactory
    default Usuario toModel(CrearUsuarioDTO dto) {
        if (dto == null) return null;
        return Usuario.toUsuario(
                null,
                dto.nombre(),
                dto.apellido(),
                dto.email(),
                dto.documentoIdentidad(),
                dto.telefono(),
                dto.idRol(),
                new java.math.BigDecimal(dto.salarioBase())
        );
    }

    RespuestaUsuarioDTO toResponse(Usuario usuario);
}
