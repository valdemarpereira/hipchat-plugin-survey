package com.valdemar.service;

import com.valdemar.dao.ClientCredentialsRepository;
import com.valdemar.model.ClientCredentialsData;
import com.valdemar.model.Token;
import okhttp3.Credentials;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class TokenStore implements InitializingBean {

    @Autowired
    ClientCredentialsRepository repository;

    @Autowired
    private Retrofit retrofit;

    private TokenService tokenService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void afterPropertiesSet() throws Exception {
        tokenService = retrofit.create(TokenService.class);
    }

    public void storeClientCredentials(ClientCredentialsData clientCredentialsData) {
        repository.save(clientCredentialsData);
    }

    public  Optional<ClientCredentialsData> getClientCredentials(String oauth) {
        return repository.findById(oauth);
    }

    public Optional<String> accessToken(String oauth) throws IOException {
        String storeToken = redisTemplate.opsForValue().get(oauth);

        if(storeToken == null){
            Optional<Token> accessToken = refreshAccessToken(oauth);

            if(accessToken.isPresent()) {
                storeToken = accessToken.get().getAccessToken();
                redisTemplate.opsForValue().set(oauth, storeToken, 50, TimeUnit.MINUTES);
            }
        }

        return Optional.of(storeToken);
    }

    private Optional<Token> refreshAccessToken(String oauth) throws IOException {
    //https://developer.atlassian.com/server/hipchat/hipchat-rest-api-access-tokens/

        Optional<ClientCredentialsData> clientCredentialsDataOpt = getClientCredentials(oauth);
        if(!clientCredentialsDataOpt.isPresent()){
            return Optional.empty();
        }

        String tokenEndpointUrl = clientCredentialsDataOpt.get().getTokenEndpointUrl();
        String password = clientCredentialsDataOpt.get().getOauthSecret();

        Token token = tokenService.token(
                tokenEndpointUrl,
                Credentials.basic(oauth, password),
                "client_credentials")
                .execute().body();

        return Optional.ofNullable(token);
    }
}
