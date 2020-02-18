### oauth2-base

`Authorization Server` `Resource Server` 同时位于一个服务内。

- Client : 第三方应用，它获得RO的授权后便可以去访问RO的资源。
- Resource Owner : 资源所有者，对资源具有授权能力的人。
- Resource Server : 资源服务器，它存储资源，并处理对资源的访问请求。
- Authorization Server : 授权服务器，它认证RO的身份，为RO提供授权审批流程，并最终颁发授权令牌(Access Token)。

#### User Account and Authentication (UAA) Server 

#### 授权方式 grant_types

OAuth2.0为了支持这些不同类型的第三方应用，提出了下面四种授权类型：

- authorization_code
- implicit
- password
- client_credentials

| 流程     | Response Type(第一次请求) | Grant Type(第二次请求) | 可带Refresh Token | **说明**                                                 |
| -------- | ------------------------- | ---------------------- | ----------------- | -------------------------------------------------------- |
| 授权码   | code                      | authorization_code     | 是                | 常规流程                                                 |
| Implicit | token                     | -                      | 否                | 适用于纯JS程序                                           |
| 用户认证 | -                         | password               | 是                | 客户端高度可信，且授权码流程不方便实施                   |
| 客户端   | -                         | client_credentials     | 否                | 客户端高度可信，拥有被操作资源(自用型)，或操作非敏感资源 |


#### 

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