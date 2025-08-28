package co.com.crediya.api;

import co.com.crediya.api.dto.CrearUsuarioDTO;
import co.com.crediya.api.mapper.UsuarioDTOMapper;
import co.com.crediya.usecase.usuario.UsuarioUseCase;
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
}
