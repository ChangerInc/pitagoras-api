package changer.pitagoras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class VertopalApi {

    @Bean
    public WebClient conectar() {
        return WebClient.create("https://api.externa.com/v1");
    }
}