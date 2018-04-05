package com.valdemar.service;

import com.valdemar.dao.ClientCredetialsRepository;
import com.valdemar.model.ClientCredentialsData;
import com.valdemar.model.Installable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class TokenStore {

    @Autowired
    ClientCredetialsRepository repository;

    public void storeClientCredentials(ClientCredentialsData clientCredentialsData) {
        repository.save(clientCredentialsData);
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
