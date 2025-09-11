package co.com.crediya.api;

import co.com.crediya.api.docs.UsuarioControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UsuarioRouterRest implements UsuarioControllerDocs {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler) {
        return route(GET("/api/v1/usuarios/documento/{documento}"), handler::escucharBuscarUsuarioPorDocumento)
                .andRoute(POST("/api/v1/usuarios"), handler::escucharGuardarUsuario)
                .andRoute(POST("/api/v1/login"), handler::escucharLogin);
    }
}
