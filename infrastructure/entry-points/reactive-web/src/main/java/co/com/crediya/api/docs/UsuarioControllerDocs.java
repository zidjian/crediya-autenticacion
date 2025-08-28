package co.com.crediya.api.docs;

import co.com.crediya.api.UsuarioHandler;
import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.dto.RespuestaUsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public interface UsuarioControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "escucharGuardarUsuario",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear usuario",
                            description = "Crea un nuevo usuario",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = CrearUsuarioDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = RespuestaUsuarioDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler);
}