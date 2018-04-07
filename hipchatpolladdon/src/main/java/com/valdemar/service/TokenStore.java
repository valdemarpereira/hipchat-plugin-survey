package com.valdemar.service;

import com.valdemar.dao.ClientCredetialsRepository;
import com.valdemar.model.ClientCredentialsData;
import com.valdemar.model.Installable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TokenStore {

    @Autowired
    ClientCredetialsRepository repository;

    @Autowired
    private Retrofit retrofit;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeClientCredentials(ClientCredentialsData clientCredentialsData) {
        repository.save(clientCredentialsData);
    }


    public String accessToken(String oauth){
        String accessToken = redisTemplate.opsForValue().get(oauth);

        if(accessToken == null){
            accessToken = refreshAccessToken();
            redisTemplate.opsForValue().set(oauth, accessToken, 50, TimeUnit.MINUTES);
        }

        return accessToken;
    }

    private String refreshAccessToken() {
//https://developer.atlassian.com/server/hipchat/hipchat-rest-api-access-tokens/

        /*
        POST /v2/oauth/token HTTP/1.1
Host: hipchat.kindredgroup.com
Authorization: Basic MTY1NGM0ZWYtMDk1NC00MzMxLWEzYTQtNDIxMzIwOTgzMzIxOlhKaUV0aW9wRkRTQUJLTXVmZzFsbm5HNzh4bGlGNFFrRGgzVFVlSEw=
Content-Type: application/x-www-form-urlencoded
Cache-Control: no-cache
Postman-Token: bd601f70-b4d5-9c87-7e31-7fd2367f16be

grant_type=client_credentials

or


curl -X POST \
  https://hipchat.kindredgroup.com/v2/oauth/token \
  -H 'authorization: Basic MTY1NGM0ZWYtMDk1NC00MzMxLWEzYTQtNDIxMzIwOTgzMzIxOlhKaUV0aW9wRkRTQUJLTXVmZzFsbm5HNzh4bGlGNFFrRGgzVFVlSEw=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'postman-token: 868111ce-7a16-a107-5a20-50b5bd6638bc' \
  -d grant_type=client_credentials


         */
        return "dummy";

    }
    /*

    function refreshAccessToken(oauthId, callback) {
    var installation = installationStore[oauthId];
    var params = {
        uri: installation.tokenUrl,
        auth: {
            username: installation['oauthId'],
            password: installation['oauthSecret']
        },
        form: {
            grant_type: 'client_credentials'
        }
    };

    request.post(params, function (err, response, body) {
        var accessToken = JSON.parse(body);

        //Store the access token
        accessTokenStore[oauthId] = {
            // Add a minute of leeway
            expirationTimeStamp: Date.now() + ((accessToken['expires_in'] - 60) * 1000),
            token: accessToken
        };
        callback(accessToken);
    });
}

A token typically expires after an hour, after which you need to generate a new one:

function isExpired(accessToken) {
    return accessToken.expirationTimeStamp < Date.now();
}

function getAccessToken(oauthId, callback) {
    var accessToken = accessTokenStore[oauthId];
    if (!accessToken || isExpired(accessToken)) {
        refreshAccessToken(oauthId, callback);
    } else {
        process.nextTick(function () {
            callback(accessToken.token);
        });
    }
}
     */
}
