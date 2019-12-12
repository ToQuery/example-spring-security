#


## oauth2

### base

http://localhost:8080/api/users/me

http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info

curl -X POST --user clientapp:123456 http://localhost:8080/oauth/token \
        -H "content-type: application/x-www-form-urlencoded" \
        -d "code=DwcTAv&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8081%2Flogin&scope=read_profile_info"


curl -X GET http://localhost:8080/api/users/me \
     -H "authorization: Bearer 41690e84-9789-4f03-8794-93f80577cb04"

     
