package com.valdemar.controller;

import com.jayway.jsonpath.JsonPath;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.valdemar.model.ClientCredentialsData;
import com.valdemar.model.Installable;
import com.valdemar.service.CapabilitiesService;
import com.valdemar.service.TokenStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AddonController implements InitializingBean {

    @Value("${application.baseUrl}")
    private String baseUrl;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private Retrofit retrofit;

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

    @Override
    public void afterPropertiesSet() throws Exception {
        service = retrofit.create(CapabilitiesService.class);

    }
}
