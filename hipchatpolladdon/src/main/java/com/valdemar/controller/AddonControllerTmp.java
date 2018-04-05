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
import com.valdemar.model.Installable;
import com.valdemar.model.LozengeType;
import com.valdemar.model.glance.Glance;
import com.valdemar.service.CapabilitiesService;
import com.valdemar.service.PollService;
import com.valdemar.service.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AddonControllerTmp {


    @Value("${application.baseUrl}")
    private String baseUrl;

    @Autowired
    private PollService pollService;

    @Autowired
    private TokenStore tokenStore;
    //ngrok

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

        String oauthId = installation.getOauthId();
        String capabilitiesUrl = installation.getCapabilitiesUrl();

        //store the installation data
        tokenStore.storeInstallation(oauthId, installation);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CapabilitiesService service = retrofit.create(CapabilitiesService.class);

        String json = service.capabilities(capabilitiesUrl).execute().body();

        // Save the token endpoint URL, API endpoint URL, 7 along with the client credentials
        String tokenUrl = JsonPath.read(json, "$.capabilities.oauth2Provider.tokenUrl");
        String capabilities = JsonPath.read(json, "$.capabilities.hipchatApiProvider.url");


        return;
    }



    @GetMapping(path= "/glance")
    public Glance glance(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> requestParams) throws FileNotFoundException {
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
        } catch (JWTDecodeException exception){
            //Invalid token
        }

        String oauthId = jwt.getIssuer();

        Installable installation = tokenStore.getInstallation(oauthId);



        // verify signature
        try {
            Algorithm algorithm = Algorithm.HMAC256(installation.getOauthSecret());
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

        return glance;
    }

    @GetMapping(path= "/sidebar")
    public Glance sidebar(@RequestHeader HttpHeaders headers) throws FileNotFoundException {
//        https://developer.atlassian.com/server/hipchat/glances/
//        https://www.hipchat.com/docs/apiv2/glances?_ga=2.71212176.1416251405.1521659281-1852571517.1521309145

        Glance glance = Glance.ofLozenge(
                String.format("<b>%s</b> Pools", pollService.numberOfOpenPolls("Dummy")),
                LozengeType.NEW,
                "New :)");

//        Glance glance = Glance.ofIcon(
//                String.format("<b>%s</b> Pools", pollService.numberOfOpenPolls("Dummy")),
//                baseUrl + "/img/poll_icon.png",
//                baseUrl + "/img/poll_icon.png"
//                );
        return glance;

    }


    @GetMapping(path= "/configure")
    public String configure(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> requestParams) throws FileNotFoundException {


        ClassLoader classLoader = getClass().getClassLoader();
        File config = new File(classLoader.getResource("views/config.hbs").getFile());
        File layout = new File(classLoader.getResource("views/layout.hbs").getFile());

        FileReader configReader = new FileReader(config);
        FileReader layoutReader = new FileReader(layout);


        Template tmpl = Mustache.compiler().compile(configReader);
        Map<String, String> data = new HashMap<String, String>();
        data.put("localBaseUrl", baseUrl);
        String body = tmpl.execute(data);

        tmpl = Mustache.compiler().escapeHTML(false).compile(layoutReader);
        data.put("title", "Does It Works??");
        data.put("localBaseUrl", baseUrl);

        data.put("body", body);
        String page = tmpl.execute(data);

        /*
        final File templateDir = config.getParentFile();
        Mustache.Compiler c = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate (String name) throws FileNotFoundException {
                return new FileReader(new File(templateDir, name));
            }
        });
        Map<String, String> data = new HashMap<String, String>();
        data.put("title", "it works?!?!");
        String text = c.compile(fileReader).execute(data);
*/





        //System.out.println(page);
        return page;
    }


    @DeleteMapping(path = "/uninstalled/{id}/")
    public void uninstall(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> requestParams, @PathVariable("id") String id){


        return;
    }

}