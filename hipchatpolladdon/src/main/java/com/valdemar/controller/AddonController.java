package com.valdemar.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jayway.jsonpath.JsonPath;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.valdemar.model.ClientCredentialsData;
import com.valdemar.model.Installable;
import com.valdemar.model.LozengeType;
import com.valdemar.model.glance.Glance;
import com.valdemar.service.CapabilitiesService;
import com.valdemar.service.PollService;
import com.valdemar.service.TokenStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AddonController implements InitializingBean {

    @Value("${application.baseUrl}")
    private String baseUrl;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private Retrofit retrofit;

    //TODO: Dummy
    @Autowired
    private PollService pollService;

    private CapabilitiesService service;

    @GetMapping(path= "/", produces = "application/json")
    public ResponseEntity<String> addonConnect() throws FileNotFoundException {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("atlassian-connect.json").getFile());

        FileReader fileReader = new FileReader(file);

        Template tmpl = Mustache.compiler().compile(fileReader);
        Map<String, String> data = new HashMap<String, String>();
        data.put("localBaseUrl", baseUrl);

        return ResponseEntity.ok(tmpl.execute(data));
    }

    @PostMapping(path = "/installable")
    public void installable(@RequestBody Installable installation) throws IOException {
        //https://developer.atlassian.com/server/hipchat/building-an-add-on-with-your-own-technology-stack/

        String capabilitiesUrl = installation.getCapabilitiesUrl();

        String json = service.capabilities(capabilitiesUrl).execute().body();

        String tokenUrl = JsonPath.read(json, "$.capabilities.oauth2Provider.tokenUrl");
        String apiProvider = JsonPath.read(json, "$.capabilities.hipchatApiProvider.url");

        ClientCredentialsData clientCredentialsData = new ClientCredentialsData();
        clientCredentialsData.setApiEndpointUrl(apiProvider);
        clientCredentialsData.setCapabilitiesUrl(installation.getCapabilitiesUrl());
        clientCredentialsData.setGroupId(installation.getGroupId());
        clientCredentialsData.setOauthId(installation.getOauthId());
        clientCredentialsData.setOauthSecret(installation.getOauthSecret());
        clientCredentialsData.setRoomId(installation.getRoomId());
        clientCredentialsData.setTokenEndpointUrl(tokenUrl);


        //store the installation data
        tokenStore.storeClientCredentials(clientCredentialsData);

        return;
    }

    @GetMapping(path= "/glance")
    public Glance glance(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> requestParams) throws IOException {
//        https://developer.atlassian.com/server/hipchat/glances/
//        https://www.hipchat.com/docs/apiv2/glances?_ga=2.71212176.1416251405.1521659281-1852571517.1521309145

        Glance glance = Glance.ofLozenge(
                String.format("<b>%s</b> Pools", pollService.numberOfOpenPolls("Dummy")),
                LozengeType.NEW,
                "New :)");


        String jwt_token = requestParams.get("signed_request");
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(jwt_token);
        } catch (JWTDecodeException exception) {
            //Invalid token
        }

        String oauthId = jwt.getIssuer();

        Optional<ClientCredentialsData> ClientCredentialsOpt = tokenStore.getClientCredentials(oauthId);

        // verify signature
        try {
            Algorithm algorithm = Algorithm.HMAC256(ClientCredentialsOpt.get().getOauthSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(oauthId)
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(jwt_token);
        } catch (UnsupportedEncodingException exception){
            exception.printStackTrace();
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception){
            exception.printStackTrace();
            //Invalid signature/claims
        }

        tokenStore.accessToken(ClientCredentialsOpt.get().getOauthId());

        return glance;
    }

        @Override
    public void afterPropertiesSet() throws Exception {
        service = retrofit.create(CapabilitiesService.class);

    }
}
