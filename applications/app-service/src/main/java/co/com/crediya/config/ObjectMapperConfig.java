package co.com.crediya.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperConfig {

    // Bean para crear una implementación perzonalizada de ObjectMapper
    @Bean
    public org.reactivecommons.utils.ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

    // Configuración para que falle al encontrar propiedades desconocidas en JSON
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return mapper;
    }
}