#


## [Project Modules (项目模块)](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#modules)

In Spring Security 3.0, the codebase was sub-divided into separate jars which more clearly separate different functionality areas and third-party dependencies. If you use Maven to build your project, these are the modules you should add to your `pom.xml`. Even if you do not use Maven, we recommend that you consult the `pom.xml` files to get an idea of third-party dependencies and versions. Another good idea is to examine the libraries that are included in the sample applications.

在Spring Security 3.0中，代码库被细分为单独的jar，这些jar更清楚地区分了不同的功能区域和第三方依赖项。如果使用Maven构建项目，则应将这些模块添加到pom.xml中。即使您不使用Maven，我们也建议您查阅`pom.xml`文件来了解第三方的依赖关系和版本。另一个好主意是检查示例应用程序中包含的库。

### 6.1 Core — `spring-security-core.jar`

This module contains core authentication and access-contol classes and interfaces, remoting support, and basic provisioning APIs. It is required by any application that uses Spring Security. It supports standalone applications, remote clients, method (service layer) security, and JDBC user provisioning. It contains the following top-level packages:

该模块包含核心身份验证和访问控制类和接口，远程支持和基本配置API。使用Spring Security的任何应用程序都需要它。它支持独立的应用程序，远程客户端，方法（服务层）安全性和JDBC用户配置。它包含以下顶级程序包：

- `org.springframework.security.core`
- `org.springframework.security.access`
- `org.springframework.security.authentication`
- `org.springframework.security.provisioning`

### 6.2 Remoting — `spring-security-remoting.jar`

This module provides integration with Spring Remoting. You do not need this unless you are writing a remote client that uses Spring Remoting. The main package is `org.springframework.security.remoting`.

该模块提供了与Spring Remoting的集成。除非您要编写使用Spring Remoting的远程客户端，否则您不需要这样做。主要包是org.springframework.security.remoting。

### 6.3 Web — `spring-security-web.jar`

This module contains filters and related web-security infrastructure code. It contains anything with a servlet API dependency. You need it if you require Spring Security web authentication services and URL-based access-control. The main package is `org.springframework.security.web`.

该模块包含过滤器和相关的Web安全基础结构代码。它包含任何与Servlet API相关的内容。如果需要Spring Security Web认证服务和基于URL的访问控制，则需要它。主要包是org.springframework.security.web。

### 6.4 Config — `spring-security-config.jar`

This module contains the security namespace parsing code and Java configuration code. You need it if you use the Spring Security XML namespace for configuration or Spring Security’s Java Configuration support. The main package is `org.springframework.security.config`. None of the classes are intended for direct use in an application.

该模块包含安全名称空间解析代码和Java配置代码。如果您使用Spring Security XML名称空间进行配置或Spring Security的Java配置支持，则需要它。主要包是 `org.springframework.security.config`。这些类都不打算直接在应用程序中使用。

### 6.5 LDAP — `spring-security-ldap.jar`

This module provides LDAP authentication and provisioning code. It is required if you need to use LDAP authentication or manage LDAP user entries. The top-level package is `org.springframework.security.ldap`.

该模块提供LDAP身份验证和供应代码。如果您需要使用LDAP认证或管理LDAP用户条目，则为必填项。顶级包是`org.springframework.security.ldap`。

### 6.6 OAuth 2.0 Core — `spring-security-oauth2-core.jar`

`spring-security-oauth2-core.jar` contains core classes and interfaces that provide support for the OAuth 2.0 Authorization Framework and for OpenID Connect Core 1.0. It is required by applications that use OAuth 2.0 or OpenID Connect Core 1.0, such as client, resource server, and authorization server. The top-level package is `org.springframework.security.oauth2.core`.

spring-security-oauth2-core.jar包含核心类和接口，这些类和接口提供对OAuth 2.0授权框架和OpenID Connect Core 1.0的支持。使用OAuth 2.0或OpenID Connect Core 1.0的应用程序（例如客户端，资源服务器和授权服务器）需要它。顶级软件包是“ org.springframework.security.oauth2.core”。

### 6.7 OAuth 2.0 Client — `spring-security-oauth2-client.jar`

`spring-security-oauth2-client.jar` contains Spring Security’s client support for OAuth 2.0 Authorization Framework and OpenID Connect Core 1.0. It is required by applications that use OAuth 2.0 Login or OAuth Client support. The top-level package is `org.springframework.security.oauth2.client`.

`spring-security-oauth2-client.jar`包含Spring Security对OAuth 2.0授权框架和OpenID Connect Core 1.0的客户端支持。使用OAuth 2.0登录或OAuth客户端支持的应用程序需要使用它。顶级软件包是“ org.springframework.security.oauth2.client”。

### 6.8 OAuth 2.0 JOSE — `spring-security-oauth2-jose.jar`

`spring-security-oauth2-jose.jar` contains Spring Security’s support for the JOSE (Javascript Object Signing and Encryption) framework. The JOSE framework is intended to provide a method to securely transfer claims between parties. It is built from a collection of specifications:

`spring-security-oauth2-jose.jar`包含Spring Security对JOSE（Javascript对象签名和加密）框架的支持。 JOSE框架旨在提供一种在各方之间安全地转移索赔的方法。它是根据一系列规范构建的：

- JSON Web Token (JWT)
- JSON Web Signature (JWS)
- JSON Web Encryption (JWE)
- JSON Web Key (JWK)

It contains the following top-level packages:

- `org.springframework.security.oauth2.jwt`
- `org.springframework.security.oauth2.jose`

### 6.9 OAuth 2.0 Resource Server — `spring-security-oauth2-resource-server.jar`

`spring-security-oauth2-resource-server.jar` contains Spring Security’s support for OAuth 2.0 Resource Servers. It is used to protect APIs via OAuth 2.0 Bearer Tokens. The top-level package is `org.springframework.security.oauth2.server.resource`.

`spring-security-oauth2-resource-server.jar`包含Spring Security对OAuth 2.0资源服务器的支持。它用于通过OAuth 2.0承载令牌保护API。顶级软件包是“ org.springframework.security.oauth2.server.resource”。

### 6.10 ACL — `spring-security-acl.jar`

This module contains a specialized domain object ACL implementation. It is used to apply security to specific domain object instances within your application. The top-level package is `org.springframework.security.acls`.

该模块包含专门的域对象ACL实现。它用于将安全性应用于应用程序中的特定域对象实例。顶级包是“ org.springframework.security.acls”。

### 6.11 CAS — `spring-security-cas.jar`

This module contains Spring Security’s CAS client integration. You should use it if you want to use Spring Security web authentication with a CAS single sign-on server. The top-level package is `org.springframework.security.cas`.

该模块包含Spring Security的CAS客户端集成。如果要对CAS单点登录服务器使用Spring Security Web认证，则应该使用它。顶级包是“ org.springframework.security.cas”。

### 6.12 OpenID — `spring-security-openid.jar`

This module contains OpenID web authentication support. It is used to authenticate users against an external OpenID server. The top-level package is `org.springframework.security.openid`. It requires OpenID4Java.

该模块包含OpenID Web身份验证支持。它用于根据外部OpenID服务器对用户进行身份验证。顶级包是“ org.springframework.security.openid”。它需要OpenID4Java。

### 6.13 Test — `spring-security-test.jar`

This module contains support for testing with Spring Security.

该模块包含对使用Spring Security进行测试的支持。

## oauth2

### oauth2-base

`Authorization Server` `Resource Server` 同时位于一个服务内。

#### 流程验证
未登录访问 [http://localhost:8080/api/users/me](http://localhost:8080/api/users/me) 获取用户信息，提示游客用户 `anonymousUser`
点击 [http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info](http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info) ，输入账号密码 `admin` `123456` 登录成功跳转 `http://localhost:8081/login?code=xxxxx`, 获取code的值将在下一步中使用。
通过 Postman 、 Http Client 或 curl 进行后续登录，使用 Http Client 在文件 [oauth2/base/src/test/resources/oauth2-base.http](oauth2/base/src/test/resources/oauth2-base.http) 中 ，替换 code ，执行请求(第一个)后，获取 access_token

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


