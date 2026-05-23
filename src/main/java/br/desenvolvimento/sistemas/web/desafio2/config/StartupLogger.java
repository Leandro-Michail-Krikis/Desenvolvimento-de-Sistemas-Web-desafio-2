package br.desenvolvimento.sistemas.web.desafio2.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    private final Environment env;

    public StartupLogger(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        String base = "http://localhost:" + port + contextPath;

        System.out.println("""
                \s
                ╔══════════════════════════════════════════════════════════╗
                ║              🚀  APLICAÇÃO INICIADA COM SUCESSO          ║
                ╠══════════════════════════════════════════════════════════╣
                ║  📋 Swagger UI  : %s/swagger-ui/index.html
                ║  📄 API Docs    : %s/v3/api-docs
                ║  🗄️  H2 Console  : %s/h2-console
                ╚══════════════════════════════════════════════════════════╝
                \s""".formatted(base, base, base));
    }
}

