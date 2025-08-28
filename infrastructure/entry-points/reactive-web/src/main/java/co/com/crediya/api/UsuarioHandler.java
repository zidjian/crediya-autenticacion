package co.com.crediya.api;

import co.com.crediya.api.dto.BuscarPorDocumentoDTO;
import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.mapper.UsuarioDTOMapper;
import co.com.crediya.usecase.usuario.UsuarioUseCase;
import co.com.crediya.usecase.usuario.exceptions.UsuarioNotFoundException;
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
    private final UsuarioDTOMapper mapper;
    private final Validator validator;

    public Mono<ServerResponse> escucharGuardarUsuario(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(CrearUsuarioDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El cuerpo de la petición es requerido")))
                .doOnSubscribe(sub -> log.info("[CREAR_USUARIO] Petición recibida"))
                .flatMap(dto -> {
                    Set<ConstraintViolation<CrearUsuarioDTO>> violaciones = validator.validate(dto);
                    if (!violaciones.isEmpty()) {
                        log.warn("[CREAR_USUARIO] Validación fallida: {} violación(es)", violaciones.size());
                        return Mono.error(new ConstraintViolationException(violaciones));
                    }
                    return Mono.just(dto);
                })
                .map(mapper::toModel)
                .flatMap(usuarioUseCase::crearUsuario)
                .doOnSuccess(u -> log.info("[CREAR_USUARIO] Usuario persistido con id={}", u.getIdUsuario()))
                .flatMap(usuarioGuardado -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toResponse(usuarioGuardado))
                )
                .doOnError(ex -> log.error("[CREAR_USUARIO] Error creando usuario: {}", ex.toString()));
    }

    public Mono<ServerResponse> escucharBuscarUsuarioPorDocumento(ServerRequest serverRequest) {
        String documentoIdentidad = serverRequest.pathVariable("documento");
        BuscarPorDocumentoDTO dto = new BuscarPorDocumentoDTO(documentoIdentidad);

        return Mono.just(dto)
                .doOnSubscribe(sub -> log.info("[BUSCAR_USUARIO] Petición recibida para documento: {}", documentoIdentidad))
                .flatMap(buscarDto -> {
                    Set<ConstraintViolation<BuscarPorDocumentoDTO>> violaciones = validator.validate(buscarDto);
                    if (!violaciones.isEmpty()) {
                        log.warn("[BUSCAR_USUARIO] Validación fallida: {} violación(es)", violaciones.size());
                        return Mono.error(new ConstraintViolationException(violaciones));
                    }
                    return Mono.just(buscarDto.documentoIdentidad());
                })
                .flatMap(usuarioUseCase::buscarUsuarioPorDocumentoIdentidad)
                .flatMap(usuario -> {
                    log.info("[BUSCAR_USUARIO] Usuario encontrado con id={}", usuario.getIdUsuario());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(mapper.toResponse(usuario));
                })
                .switchIfEmpty(
                    Mono.defer(() -> {
                        log.info("[BUSCAR_USUARIO] Usuario no encontrado para documento: {}", documentoIdentidad);
                        return Mono.error(new UsuarioNotFoundException("No existe un usuario con el documento de identidad proporcionado: " + documentoIdentidad));
                    })
                )
                .doOnError(ex -> log.error("[BUSCAR_USUARIO] Error buscando usuario por documento: {}", ex.toString()));
    }
}
