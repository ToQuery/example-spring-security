package io.github.toquery.example.spring.security.oauth2.reactive.resource;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class ExampleSpringSecurityOauth2ReactiveResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringSecurityOauth2ReactiveResourceServerApplication.class, args);
    }

}
