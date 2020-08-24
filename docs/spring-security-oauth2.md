## [Oauth 流程规范](https://oauth.net/2/)

### OAuth 2.0 Framework -  [RFC 6749](https://tools.ietf.org/html/rfc6749)
OAuth Scope

### 授权方式 OAuth Grant Types

OAuth2 为了支持不同类型的第三方应用，提出了下面六种授权类型：

- Authorization Code [RFC 6749#section-1.3.1](https://tools.ietf.org/html/rfc6749#section-1.3.1)

```text

  +--------+                                           +---------------+
  |        |--(A)------- Authorization Grant --------->|               |
  |        |                                           |               |
  |        |<-(B)----------- Access Token -------------|               |
  |        |               & Refresh Token             |               |
  |        |                                           |               |
  |        |                            +----------+   |               |
  |        |--(C)---- Access Token ---->|          |   |               |
  |        |                            |          |   |               |
  |        |<-(D)- Protected Resource --| Resource |   | Authorization |
  | Client |                            |  Server  |   |     Server    |
  |        |--(E)---- Access Token ---->|          |   |               |
  |        |                            |          |   |               |
  |        |<-(F)- Invalid Token Error -|          |   |               |
  |        |                            +----------+   |               |
  |        |                                           |               |
  |        |--(G)----------- Refresh Token ----------->|               |
  |        |                                           |               |
  |        |<-(H)----------- Access Token -------------|               |
  +--------+           & Optional Refresh Token        +---------------+

               Figure 1: Refreshing an Expired Access Token
 
   The flow illustrated in Figure 1 includes the following steps:

   (A)  The client requests an access token by authenticating with the
        authorization server and presenting an authorization grant.

   (B)  The authorization server authenticates the client and validates
        the authorization grant, and if valid, issues an access token
        and a refresh token.

   (C)  The client makes a protected resource request to the resource
        server by presenting the access token.

   (D)  The resource server validates the access token, and if valid,
        serves the request.

   (E)  Steps (C) and (D) repeat until the access token expires.  If the
        client knows the access token expired, it skips to step (G);
        otherwise, it makes another protected resource request.

   (F)  Since the access token is invalid, the resource server returns
        an invalid token error.

   (G)  The client requests a new access token by authenticating with
        the authorization server and presenting the refresh token.  The
        client authentication requirements are based on the client type
        and on the authorization server policies.

   (H)  The authorization server authenticates the client and validates
        the refresh token, and if valid, issues a new access token (and,
        optionally, a new refresh token).
```

> 授权码方式

- PKCE [RFC 7636](https://tools.ietf.org/html/rfc7636)

```text
                                                 +-------------------+
                                                 |   Authz Server    |
       +--------+                                | +---------------+ |
       |        |--(A)- Authorization Request ---->|               | |
       |        |       + t(code_verifier), t_m  | | Authorization | |
       |        |                                | |    Endpoint   | |
       |        |<-(B)---- Authorization Code -----|               | |
       |        |                                | +---------------+ |
       | Client |                                |                   |
       |        |                                | +---------------+ |
       |        |--(C)-- Access Token Request ---->|               | |
       |        |          + code_verifier       | |    Token      | |
       |        |                                | |   Endpoint    | |
       |        |<-(D)------ Access Token ---------|               | |
       +--------+                                | +---------------+ |
                                                 +-------------------+

                     Figure 2: Abstract Protocol Flow

   This specification adds additional parameters to the OAuth 2.0
   Authorization and Access Token Requests, shown in abstract form in
   Figure 2.

   A. The client creates and records a secret named the "code_verifier"
      and derives a transformed version "t(code_verifier)" (referred to
      as the "code_challenge"), which is sent in the OAuth 2.0
      Authorization Request along with the transformation method "t_m".

   B. The Authorization Endpoint responds as usual but records
      "t(code_verifier)" and the transformation method.

   C. The client then sends the authorization code in the Access Token
      Request as usual but includes the "code_verifier" secret generated
      at (A).

   D. The authorization server transforms "code_verifier" and compares
      it to "t(code_verifier)" from (B).  Access is denied if they are
      not equal.
```

> 基于授权码方式的拓展

- Client Credentials

> 

- Device Code [RFC 8628#section-3.4](https://tools.ietf.org/html/rfc8628#section-3.4)

```text
      +----------+                                +----------------+
      |          |>---(A)-- Client Identifier --->|                |
      |          |                                |                |
      |          |<---(B)-- Device Code,      ---<|                |
      |          |          User Code,            |                |
      |  Device  |          & Verification URI    |                |
      |  Client  |                                |                |
      |          |  [polling]                     |                |
      |          |>---(E)-- Device Code       --->|                |
      |          |          & Client Identifier   |                |
      |          |                                |  Authorization |
      |          |<---(F)-- Access Token      ---<|     Server     |
      +----------+   (& Optional Refresh Token)   |                |
            v                                     |                |
            :                                     |                |
           (C) User Code & Verification URI       |                |
            :                                     |                |
            v                                     |                |
      +----------+                                |                |
      | End User |                                |                |
      |    at    |<---(D)-- End user reviews  --->|                |
      |  Browser |          authorization request |                |
      +----------+                                +----------------+

                    Figure 3: Device Authorization Flow

The device authorization flow illustrated in Figure 3 includes the
   following steps:

   (A)  The client requests access from the authorization server and
        includes its client identifier in the request.

   (B)  The authorization server issues a device code and an end-user
        code and provides the end-user verification URI.

   (C)  The client instructs the end user to use a user agent on another
        device and visit the provided end-user verification URI.  The
        client provides the user with the end-user code to enter in
        order to review the authorization request.

   (D)  The authorization server authenticates the end user (via the
        user agent), and prompts the user to input the user code
        provided by the device client.  The authorization server
        validates the user code provided by the user, and prompts the
        user to accept or decline the request.

   (E)  While the end user reviews the client's request (step D), the
        client repeatedly polls the authorization server to find out if
        the user completed the user authorization step.  The client
        includes the device code and its client identifier.

   (F)  The authorization server validates the device code provided by
        the client and responds with the access token if the client is
        granted access, an error if they are denied access, or an
        indication that the client should continue to poll.
```

> 设备码模式一般用于一些没有浏览器，缺少输入条件的设备上，比如 Apple TV，可穿戴设备等。
> 设备令牌的流程和其他流程稍有不同，首先设备需要从授权服务器获取一个 uri 和一个用户码，接着用户需要通过浏览器打开 uri，输入这个用户码。在这个过程中，设备会一直循环，尝试去获取 token，直到拿到 token 或者用户码过期。
> 我们现在常用的二维码扫码登录流程，可以使用这个模式实现。

- Refresh Token [RFC 6749#section-1.5](https://tools.ietf.org/html/rfc6749#section-1.5)

>

- Legacy: Implicit Flow [RFC 6749#section-1.3.2](https://tools.ietf.org/html/rfc6749#section-1.3.2)

>

- Legacy: Password Grant

>



| 授权类型           | Response Type(第一次请求) | Grant Type(第二次请求) | 可带Refresh Token | **说明**                                                 |
| ------------------ | ------------------------- | ---------------------- | ----------------- | -------------------------------------------------------- |
| Authorization Code | code                      | authorization_code     | 是                | 常规流程                                                 |
| PKCE               |                           |                        |                   |                                                          |
| Client Credentials | -                         | client_credentials     | 否                | 客户端高度可信，拥有被操作资源(自用型)，或操作非敏感资源 |
| Device Code        |                           |                        |                   |                                                          |
| Refresh Token      |                           |                        |                   |                                                          |
| Implicit Flow      | token                     | -                      | 否                | 适用于纯JS程序                                           |
| Password Grant     | -                         | password               | 是                | 客户端高度可信，且授权码流程不方便实施                   |

### Client Types - Confidential and Public Applications

   OAuth根据两种客户端类型向授权服务器进行安全身份验证的能力，定义了两种客户端类型(e.g., 维护其客户凭证机密性的能力):

   confidential
      能够维护其证书机密性的客户端(e.g., 在受限制访问客户端凭据的安全服务器上实施的客户端),或能够使用其他方式进行安全的客户端身份验证。

   public
      无法维护其证书机密性的客户(e.g., 在资源所有者使用的设备上执行的客户端，例如已安装的本机应用程序或基于Web浏览器的应用程序), 在资源所有者使用的设备上执行的客户端，例如已安装的本机应用程序或基于Web浏览器的应用程序


   客户端类型的指定基于授权服务器对安全认证的定义及其可接受的客户端凭据公开级别。授权服务器不应对客户端类型做任何假设

   客户端可以实现为一组分布式组件，每个组件具有不同的客户端类型和安全上下文(e.g., 具有基于服务器的机密组件和基于公共浏览器的组件的分布式客户端)。
   如果授权服务器不为此类客户端提供支持或不提供有关其注册的指导，则客户端应将每个组件注册为单独的客户端。

   该规范是围绕以下客户端配置文件设计的：

   web application
      Web应用程序是在Web服务器上运行的机密客户端。资源所有者通过在资源所有者使用的设备上的用户代理中呈现的HTML用户界面访问客户端。客户端凭据以及颁发给客户端的任何访问令牌都存储在Web服务器上，并且资源所有者无法访问或访问。

   user-agent-based application
      基于用户代理的应用程序是一个公共客户端，其中客户端代码是从Web服务器下载的，并在资源所有者使用的设备上的用户代理（e.g., Web浏览器）中执行。协议数据和凭据易于资源所有者访问（并且经常可见）。
      由于此类应用程序驻留在用户代理中，因此它们可以在请求授权时无缝使用用户代理功能。

   native application
      本机应用程序是在资源所有者使用的设备上安装并执行的公共客户端。协议数据和凭据可供资源所有者访问。假定可以提取应用程序中包含的任何客户端身份验证凭据。另一方面，动态颁发的凭据（例如访问令牌或刷新令牌）可以收到可接受的保护级别。
      至少，应保护这些凭据不受可能与应用程序进行交互的敌对服务器的攻击。在某些平台上，可以保护这些凭据免受驻留在同一设备上的其他应用程序的攻击。


### oauth2-base

`Authorization Server` `Resource Server` 同时位于一个服务内。

- Client : 第三方应用，它获得RO的授权后便可以去访问RO的资源。
- Resource Owner : 资源所有者，对资源具有授权能力的人。
- Resource Server : 资源服务器，它存储资源，并处理对资源的访问请求。
- Authorization Server : 授权服务器，它认证RO的身份，为RO提供授权审批流程，并最终颁发授权令牌(Access Token)。

#### User Account and Authentication (UAA) Server 

| 项目                                       | 语言 |
| ------------------------------------------ | ---- |
| [keycloak](https://www.keycloak.org/)      | java |
| [uaa](https://github.com/cloudfoundry/uaa) | java |
| [hydra](https://github.com/ory/hydra)      | go   |



#### OAuth2异构系统用户登录配置

![](images/spring-boot-authentication-spring-security-architecture.png)

spring-security-oauth2 authorization_code模式登录流程：

1、#OAuth2AuthorizationRequestRedirectFilter 拦截授权请求：/oauth2/authorization/{registrationId},
    如果匹配则创建#OAuth2AuthorizationRequest 对象(见#DefaultOAuth2AuthorizationRequestResolver)
    紧接着sendRedirect(#OAuth2AuthorizationRequest)。此即为authorization_code模式第一步(获取code)
2、#OAuth2LoginAuthenticationFilter 拦截OAuth2Provider发来的携带了code的callback请求：/login/oauth2/code/{registrationId}
    如果匹配则从请求参数中提取code参数,然后执行认证(见#OAuth2LoginAuthenticationFilter.attemptAuthentication)：
    2.1 标准OAuth2授权的code换 token：#OAuth2LoginAuthenticationProvider (如果scope中不包含'openid')
    2.2 标准OIDC授权的code换token：#OidcAuthorizationCodeAuthenticationProvider (如果scope中包含'openid')
        其中的code换token正是通过 OAuth2Provider.tokenUri 标识的请求来完成的
        通过code拿到的JWT令牌需要通过 OAuth2Provider.jwkSetUri 获取到的jwks来进行校验

#### 流程验证
未登录访问 [http://localhost:8080/api/users/me](http://localhost:8080/api/users/me) 获取用户信息，提示游客用户 `anonymousUser`
点击 [http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info](http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info) ，输入账号密码 `admin` `123456` 登录成功跳转 `http://localhost:8081/login?code=xxxxx`, 获取code的值将在下一步中使用。
通过 Postman 、 Http Client 或 curl 进行后续登录，使用 Http Client 在文件 [oauth2-base.http](/bin/http/oauth2-base.http) 中 ，替换 code ，执行请求(第一个)后，获取 access_token

```shell script
curl -X POST --user clientapp:123456 http://localhost:8080/oauth/token \
        -H "content-type: application/x-www-form-urlencoded" \
        -d "code=u028mn&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8081%2Flogin&scope=read_profile_info"
```

响应数据:

```json
{
  "access_token": "ceb937a3-82e6-4708-8348-f0cfe5210ada",
  "token_type": "bearer",
  "refresh_token": "4aedfd29-9ddb-4359-8cbd-668faf847d2d",
  "expires_in": 4999,
  "scope": "read_profile_info"
}
```

替换新的 access_token 后执行请求(第二个)，获取到登录用户数据，见文件 [oauth2/base/base/src/test/resources/user-me.json](oauth2/base/base/src/test/resources/user-me.json)

```shell script
curl -X GET http://localhost:8080/api/users/me -H "Authorization: Bearer ceb937a3-82e6-4708-8348-f0cfe5210ada"
```
## 参考资料

- https://github.com/spring-projects-experimental/spring-authorization-server
