package io.github.toquery.example.spring.security.oauth2.resource;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

public class OAuth2ResourceServer  extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/").permitAll();
    }

    /* 需要与 OAuth2AuthorizationServer#configure 的 resourceIds 配合 */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("example-spring-security-oauth2-resource-server").stateless(true);;
    }
}
