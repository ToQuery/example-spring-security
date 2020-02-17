package io.github.toquery.example.spring.security.oauth2.reactive.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableAuthorizationServer
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class OAuth2ReactiveAuthorizationServerSecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 这里应该用于客户端
    @Bean
    ReactiveClientRegistrationRepository clientRegistrations() {
//                .resourceIds("example-spring-security-oauth2-resource-server")
//                // 自动授权，无需人工手动点击 approve
//                .autoApprove(true)
//                .redirectUris("http://localhost:8080/login/oauth2/code/gateway")
//                .accessTokenValiditySeconds(120)
//                .refreshTokenValiditySeconds(240000);

        ClientRegistration clientRegistration = ClientRegistrations
                .fromIssuerLocation("https://idp.example.com/auth/realms/demo")
                .clientId("app-1")
                .clientSecret(passwordEncoder.encode("123456"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.IMPLICIT)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .scope("openid", "profile", "email", "resource.read")
                .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }


    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/about").permitAll()
                .anyExchange().authenticated()
                .and().oauth2Login()
                .and().build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    // @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }


    //@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("app-1")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("password", "implicit", "authorization_code", "refresh_token", "client_credentials")
                .authorities("uaa.resource")
                .scopes("openid", "profile", "email", "resource.read")
                .resourceIds("example-spring-security-oauth2-resource-server")
                // 自动授权，无需人工手动点击 approve
                .autoApprove(true)
                .redirectUris("http://localhost:8080/login/oauth2/code/gateway")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240000);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
