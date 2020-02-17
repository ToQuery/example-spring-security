package io.github.toquery.example.spring.security.oauth2.reactive.authorization;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ExampleSpringSecurityOauth2AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringSecurityOauth2AuthorizationServerApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
