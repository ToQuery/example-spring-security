package io.github.toquery.example.spring.security.oauth2.reactive.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class OAuth2ReactiveAuthorizationServerSecurityConfig {



    public static final ClientSettings defaultClientSettings = ClientSettings.builder()
            // requireProofKey: 当参数为true时，该客户端仅支持PCKE
            // .requireProofKey(true)
            // .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
            // requireAuthorizationConsent: 是否需要授权统同意
            .requireAuthorizationConsent(false)
            .build();

    public static final TokenSettings defaultTokenSettings = TokenSettings.builder()
            // accessTokenFormat: 访问令牌格式，支持OAuth2TokenFormat.SELF_CONTAINED（自包含的令牌使用受保护的、有时间限制的数据结构，例如JWT）；OAuth2TokenFormat.REFERENCE（不透明令牌）
            //使用透明方式，
            // 默认是 OAuth2TokenFormat SELF_CONTAINED  全的jwt token
            // REFERENCE 是引用方式，即使用jwt token，但是jwt token是通过oauth2 server生成的，而不是通过oauth2 client生成的
            // .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            // access_token 有效期
            .accessTokenTimeToLive(Duration.ofHours(6))
            // refresh_token 有效期
            .refreshTokenTimeToLive(Duration.ofDays(3))
            // reuseRefreshTokens 是否重用刷新令牌。当参数为true时，刷新令牌后不会重新生成新的refreshToken
            .reuseRefreshTokens(true)
            .build();

    public static final RegisteredClient defaultRegisteredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(UUID.randomUUID().toString())
            .clientSecret(UUID.randomUUID().toString())

            // 客户端可能使用的身份验证方法。支持的值为client_secret_basic、client_secret_post、private_key_jwt、client_secret_jwt和none
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            // 客户端可以使用的授权类型。支持的值为authorization_code、implicit、password、client_credentials、refresh_token和urn:ietf:params:oauth:grant-type:jwt-bearer
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
            .redirectUri("http://127.0.0.1:8010/login/oauth2/code/toquery")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.EMAIL)
            .scope(OidcScopes.ADDRESS)
            .scope(OidcScopes.PHONE)
            .scope("read")
            .scope("write")
            .clientSettings(defaultClientSettings)
            .tokenSettings(defaultTokenSettings)
            .build();

    // 这里应该用于客户端
    @Bean
    public ReactiveClientRegistrationRepository clientRegistrations() {
//                .resourceIds("example-spring-security-oauth2-resource-server")
//                // 自动授权，无需人工手动点击 approve
//                .autoApprove(true)
//                .redirectUris("http://localhost:8080/login/oauth2/code/gateway")
//                .accessTokenValiditySeconds(120)
//                .refreshTokenValiditySeconds(240000);

        RegisteredClient example = RegisteredClient.from(defaultRegisteredClient)
                .id(UUID.randomUUID().toString())
                .clientId("app-1")
                .clientName("app-1")
                .clientSecret("{noop}123456")
                .redirectUri("http://127.0.0.1:1234/oidc-client/sample.html")
                .redirectUri("http://127.0.0.1:1234/code-flow-duendesoftware/sample.html")
                .redirectUri("http://127.0.0.1:1234/code-flow-duendesoftware/sample-silent.html")
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


}
