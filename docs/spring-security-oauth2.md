### oauth2-base

`Authorization Server` `Resource Server` 同时位于一个服务内。

#### User Account and Authentication (UAA) Server 

#### 授权类型 grant_types

- authorization_code
- implicit
- password
- client_credentials


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
