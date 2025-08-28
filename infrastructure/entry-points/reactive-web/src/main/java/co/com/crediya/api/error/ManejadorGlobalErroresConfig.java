package co.com.crediya.api.error;

import co.com.crediya.usecase.usuario.exceptions.NegocioException;
import co.com.crediya.usecase.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.usecase.usuario.exceptions.UsuarioNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.crediya.shared.error.ErrorDetail;
import co.com.crediya.shared.error.ErrorResponse;


import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ManejadorGlobalErroresConfig {
    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalExceptionHandler(DefaultErrorAttributes errorAttributes,
                                                           ApplicationContext applicationContext) {
        WebProperties.Resources resources = new WebProperties.Resources();
        return new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext);
    }

    static class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

        GlobalErrorWebExceptionHandler(DefaultErrorAttributes g, WebProperties.Resources r, ApplicationContext c) {
            super(g, r, c);
            super.setMessageReaders(ServerCodecConfigurer.create().getReaders());
            super.setMessageWriters(ServerCodecConfigurer.create().getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
            return RouterFunctions.route(
                    RequestPredicates.all()
                            .and(RequestPredicates.path("/webjars/**").negate()),
                    this::renderErrorResponse
            );
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            Throwable ex = getError(request);

            String idCorrelacion = request.headers().firstHeader("X-Correlation-Id");

            HttpStatus status;
            ErrorResponse payload;

            if (ex instanceof UsuarioYaExisteException uae) {
                status = HttpStatus.CONFLICT;
                payload = ErrorResponse.of(
                        uae.getCode(),
                        "El usuario ya existe: " + uae.getEmail(),
                        status.value(),
                        request.path(),
                        null,
                        idCorrelacion
                );
            } else if (ex instanceof UsuarioNotFoundException unfe) {
                status = HttpStatus.NOT_FOUND;
                payload = ErrorResponse.of(
                        "USUARIO_NO_ENCONTRADO",
                        unfe.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        idCorrelacion
                );
            } else if (ex instanceof NegocioException be) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorResponse.of(
                        be.getCode(),
                        be.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        idCorrelacion
                );
            } else if (ex instanceof ConstraintViolationException cve) {
                status = HttpStatus.BAD_REQUEST;
                List<ErrorDetail> detalles = cve.getConstraintViolations().stream()
                        .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                        .collect(Collectors.toList());
                payload = ErrorResponse.of(
                        "ERROR_VALIDACION",
                        "Datos de entrada inválidos",
                        status.value(),
                        request.path(),
                        detalles,
                        idCorrelacion
                );
            } else if (ex instanceof IllegalArgumentException iae) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorResponse.of(
                        "ARGUMENTO_INVALIDO",
                        iae.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        idCorrelacion
                );
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                payload = ErrorResponse.of(
                        "ERROR_INTERNO",
                        "Ocurrió un error inesperado",
                        status.value(),
                        request.path(),
                        null,
                        idCorrelacion
                );
            }

            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload));
        }
    }
}
