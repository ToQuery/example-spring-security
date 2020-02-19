package io.github.toquery.example.spring.security.acl;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ExampleSpringSecurityAclApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringSecurityAclApplication.class, args);
    }

}
