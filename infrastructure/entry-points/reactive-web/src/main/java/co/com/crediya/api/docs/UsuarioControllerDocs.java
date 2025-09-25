package co.com.crediya.api.docs;

import co.com.crediya.api.UsuarioHandler;
import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.dto.LoginDTO;
import co.com.crediya.api.dto.RespuestaLoginDTO;
import co.com.crediya.api.dto.RespuestaUsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
                            security = @SecurityRequirement(name = "bearerAuth"),
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = CrearUsuarioDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = RespuestaUsuarioDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "Token inválido o expirado"),
                                    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes"),
                                    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "escucharBuscarUsuarioPorId",
                    operation = @Operation(
                            operationId = "getUserById",
                            summary = "Buscar usuario por ID",
                            description = "Busca un usuario por su ID único. Retorna el usuario completo si existe, o un error 404 si no se encuentra",
                            tags = {"Usuarios"},
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID único del usuario a buscar",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "integer", format = "int64"),
                                            example = "1"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = RespuestaUsuarioDTO.class))),
                                    @ApiResponse(responseCode = "401", description = "Token inválido o expirado"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                                    @ApiResponse(responseCode = "400", description = "ID de usuario inválido")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "escucharLogin",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Iniciar sesion",
                            description = "Inicia sesión con las credenciales del usuario",
                            tags = {"Login"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Inicio de sesión exitoso",
                                            content = @Content(schema = @Schema(implementation = RespuestaLoginDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler);
}