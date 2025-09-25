package co.com.crediya.api;

import co.com.crediya.api.dto.BuscarPorIdDTO;
import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.dto.LoginDTO;
import co.com.crediya.api.mapper.UsuarioDTOMapper;
import co.com.crediya.api.security.JwtService;
import co.com.crediya.api.security.PasswordService;
import co.com.crediya.usecase.autenticacion.AutenticacionUseCase;
import co.com.crediya.usecase.usuario.RolUseCase;
import co.com.crediya.usecase.usuario.UsuarioUseCase;
import co.com.crediya.usecase.usuario.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioHandler {
    private final UsuarioUseCase usuarioUseCase;
    private final RolUseCase rolUseCase;
    private final AutenticacionUseCase autenticacionUseCase;
    private final UsuarioDTOMapper mapper;
    private final Validator validator;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    public Mono<ServerResponse> escucharGuardarUsuario(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(CrearUsuarioDTO.class)
                .doOnSubscribe(sub -> log.info("[CREAR_USUARIO] Petición recibida"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<CrearUsuarioDTO>> violaciones = validator.validate(dto);
                    if (!violaciones.isEmpty()) {
                        log.warn("[CREAR_USUARIO] Validación fallida: {} violación(es)", violaciones.size());
                        return Mono.error(new ConstraintViolationException(violaciones));
                    }
                    return Mono.just(dto);
                })
                .flatMap(dto ->
                        rolUseCase.findByIdRol(dto.idRol())
                                .then(Mono.just(dto))
                )
                .map(dto -> {
                    // Hash the password before creating the user
                    String hashedPassword = passwordService.hashPassword(dto.contrasenia());
                    return new CrearUsuarioDTO(
                            dto.nombre(),
                            dto.apellido(),
                            dto.email(),
                            dto.documentoIdentidad(),
                            dto.telefono(),
                            dto.idRol(),
                            dto.salarioBase(),
                            hashedPassword
                    );
                })
                .map(mapper::toModel)
                .flatMap(usuarioUseCase::crearUsuario)
                .doOnSuccess(u -> log.info("[CREAR_USUARIO] Usuario persistido con id={}", u.getIdUsuario()))
                .flatMap(usuarioGuardado ->
                        rolUseCase.findByIdRol(usuarioGuardado.getIdRol())
                                .flatMap(rol -> ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(mapper.toResponse(usuarioGuardado, rol))
                                )
                )
                .doOnError(ex -> log.error("[CREAR_USUARIO] Error creando usuario: {}", ex.toString()));
    }

    public Mono<ServerResponse> escucharBuscarUsuarioPorId(ServerRequest serverRequest) {
        String idParam = serverRequest.pathVariable("id");
        Long idUsuario;
        try {
            idUsuario = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            return Mono.error(new ValidationException("El ID debe ser un número válido"));
        }

        BuscarPorIdDTO dto = new BuscarPorIdDTO(idUsuario);

        return Mono.just(dto)
                .doOnSubscribe(sub -> log.info("[BUSCAR_USUARIO] Petición recibida para ID: {}", idUsuario))
                .flatMap(buscarDto -> {
                    Set<ConstraintViolation<BuscarPorIdDTO>> violaciones = validator.validate(buscarDto);
                    if (!violaciones.isEmpty()) {
                        log.warn("[BUSCAR_USUARIO] Validación fallida: {} violación(es)", violaciones.size());
                        return Mono.error(new ConstraintViolationException("", violaciones));
                    }
                    return Mono.just(buscarDto.idUsuario());
                })
                .flatMap(usuarioUseCase::buscarUsuarioPorId)
                .doOnSuccess(usuario -> log.info("[BUSCAR_USUARIO] Usuario encontrado con id={}", usuario.getIdUsuario()))
                .flatMap(usuario ->
                        rolUseCase.findByIdRol(usuario.getIdRol())
                                .flatMap(rol -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(mapper.toResponse(usuario, rol))
                                )
                )
                .doOnError(ex -> log.error("[BUSCAR_USUARIO] Error buscando usuario por ID: {}", ex.toString()));
    }

    public Mono<ServerResponse> escucharLogin(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(LoginDTO.class)
                .doOnSubscribe(sub -> log.info("[LOGIN] Petición de login recibida"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<LoginDTO>> violaciones = validator.validate(dto);
                    if (!violaciones.isEmpty()) {
                        log.warn("[LOGIN] Validación fallida: {} violación(es)", violaciones.size());
                        return Mono.error(new ConstraintViolationException(violaciones));
                    }
                    return Mono.just(dto);
                })
                .flatMap(dto -> autenticacionUseCase.autenticarUsuario(dto.email(), dto.contrasenia())
                        .flatMap(usuario -> {
                            // Verify password
                            if (passwordService.verifyPassword(dto.contrasenia(), usuario.getContrasenia())) {
                                // Get role information before generating token
                                return rolUseCase.findByIdRol(usuario.getIdRol())
                                        .flatMap(rol -> {
                                            // Generate JWT token with role name
                                            String token = jwtService.generateToken(
                                                    usuario.getIdUsuario(),
                                                    usuario.getEmail(),
                                                    usuario.getNombre(),
                                                    usuario.getApellido(),
                                                    usuario.getDocumentoIdentidad(),
                                                    rol.getNombre()
                                            );
                                            return Mono.just(mapper.toLoginResponse(usuario, token));
                                        });
                            } else {
                                log.warn("[LOGIN] Contraseña incorrecta para email: {}", dto.email());
                                return Mono.error(new ValidationException("Credenciales inválidas"));
                            }
                        })
                )
                .doOnSuccess(response -> log.info("[LOGIN] Login exitoso para usuario: {}", response.email()))
                .flatMap(loginResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loginResponse)
                )
                .doOnError(ex -> log.error("[LOGIN] Error en login: {}", ex.toString()));
    }
}
