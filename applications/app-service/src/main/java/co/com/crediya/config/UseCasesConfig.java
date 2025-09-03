package co.com.crediya.config;

import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.usuario.RolUseCase;
import co.com.crediya.usecase.usuario.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioRepository usuarioRepository) {
        return new UsuarioUseCase(usuarioRepository);
    }

    @Bean
    public RolUseCase rolUseCase(RolRepository rolRepository) {
        return new RolUseCase(rolRepository);
    }
}
