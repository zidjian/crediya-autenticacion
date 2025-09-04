package co.com.crediya.api;

import co.com.crediya.api.docs.UsuarioControllerDocs;
import co.com.crediya.api.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UsuarioRouterRest implements UsuarioControllerDocs {

    private final AuthorizationService authorizationService;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler) {
        return route(POST("/api/v1/usuarios"),
                request -> authorizationService.authorizeRoles(
                    request,
                    Arrays.asList("administrador", "asesor"),
                    handler::escucharGuardarUsuario
                ))
                .andRoute(GET("/api/v1/usuarios/documento/{documento}"),
                    request -> authorizationService.requireAuthentication(
                        request,
                        handler::escucharBuscarUsuarioPorDocumento
                    ))
                .andRoute(POST("/api/v1/login"), handler::escucharLogin);
    }
}
