package br.desenvolvimento.sistemas.web.desafio2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio 2 - API de Destinos")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de destinos turísticos. " +
                                "Permite consultar, criar, atualizar e remover destinos do banco de dados.")
                        .contact(new Contact()
                                .name("Desenvolvimento de Sistemas Web")
                                .email("contato@desafio2.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}

