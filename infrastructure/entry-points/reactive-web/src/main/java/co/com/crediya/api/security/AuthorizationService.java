package co.com.crediya.api.security;

import co.com.crediya.usecase.usuario.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    private final JwtService jwtService;
    private static final String TOKEN_PREFIX = "Bearer ";

    public Mono<String> validateTokenAndGetRole(ServerRequest request) {
        return Mono.fromCallable(() -> {
            String authHeader = request.headers().firstHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
                throw new UnauthorizedException("Token no proporcionado");
            }

            String token = authHeader.substring(TOKEN_PREFIX.length());

            try {
                if (jwtService.isTokenExpired(token)) {
                    throw new UnauthorizedException("Token expirado");
                }

                return jwtService.getRolFromToken(token);
            } catch (Exception e) {
                log.error("Error validando token: {}", e.getMessage());
                throw new UnauthorizedException("Token inválido");
            }
        });
    }

    public Mono<ServerResponse> authorizeRoles(ServerRequest request, List<String> allowedRoles,
                                             java.util.function.Function<ServerRequest, Mono<ServerResponse>> handler) {
        return validateTokenAndGetRole(request)
            .flatMap(role -> {
                if (allowedRoles.contains(role)) {
                    return handler.apply(request);
                } else {
                    log.warn("Acceso denegado para rol: {} en ruta: {}", role, request.path());
                    // Lanzar excepción para que sea manejada por GlobalErrorHandler
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado: rol insuficiente");
                }
            });
            // Remover onErrorResume para que los errores lleguen al GlobalErrorHandler
    }

    public Mono<ServerResponse> requireAuthentication(ServerRequest request,
                                                    java.util.function.Function<ServerRequest, Mono<ServerResponse>> handler) {
        return validateTokenAndGetRole(request)
            .flatMap(role -> handler.apply(request));
            // Remover onErrorResume para que los errores lleguen al GlobalErrorHandler
    }

    // Crear una excepción de negocio que extienda BusinessException para mejor manejo
    public static class UnauthorizedException extends BusinessException {
        public UnauthorizedException(String message) {
            super("AUTH_001", message, 401);
        }
    }
}
