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
import com.valdemar.model.Survey.Survey;
import com.valdemar.model.Survey.SurveyQuestion;
import com.valdemar.model.glance.Glance;
import com.valdemar.service.CapabilitiesService;
import com.valdemar.service.PollService;
import com.valdemar.service.TokenStore;
import j2html.tags.DomContent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import static j2html.TagCreator.*;
import static java.lang.Math.round;

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
        DecodedJWT jwt = extractAndVerifyJWTSignature(jwt_token);

        String oauthId = jwt.getIssuer();

        Optional<ClientCredentialsData> ClientCredentialsOpt = tokenStore.getClientCredentials(oauthId);

        tokenStore.accessToken(ClientCredentialsOpt.get().getOauthId());

        return glance;
    }

    @GetMapping(path= "/sidebar")
    public String sidebar(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> requestParams) throws IOException {


        String jwt_token = requestParams.get("signed_request");
        DecodedJWT jwt = extractAndVerifyJWTSignature(jwt_token);

        List<Survey> surveyList = pollService.getAllOpenSurveys();

        KindredColoursIterator colours = new KindredColoursIterator();

        String htmlQuestions = section(attrs(".aui-connect-page"),
                section(attrs(".aui-connect-content with-list"),
                            ol(attrs(".aui-connect-list"),
                                    each(surveyList, survey ->
                                        li(attrs(".aui-connect-list-item " + colours.next()),
                                            generateMenu(),
                                            span(attrs(".aui-avatar aui-avatar-xsmall"),
                                                    span(attrs(".aui-avatar-inner"),
                                                            setSurveyIcon(jwt.getIssuer(), survey.getUserId())
                                                    )
                                             ),
                                                span(attrs(".aui-connect-list-item-title bold"), survey.getTitle()),
                                                ul(attrs(".aui-connect-list-item-attributes"),
                                                        li(survey.getAuthor()),
                                                        li(survey.getEndDate().toString())
                                                    ),
                                                dl(
                                                        each( survey.getQuestions(), question ->
                                                                dd(
                                                                        attrs(".percentage percentage-" + calculatePercentage(survey, question)),
                                                                        span(attrs(".text"),question.getQuestion())
                                                                )
                                                        )
                                                )
                                    )
                            )
                        )
                )
        ).render();

        ClassLoader classLoader = getClass().getClassLoader();
        File sidebarView = new File(classLoader.getResource("views/sidebar.hbs").getFile());
        File layout = new File(classLoader.getResource("views/layout.hbs").getFile());

        FileReader sidebarViewReader = new FileReader(sidebarView);
        FileReader layoutReader = new FileReader(layout);

        Template tmpl = Mustache.compiler().escapeHTML(false).compile(sidebarViewReader);
        Map<String, String> data = new HashMap<String, String>();
        data.put("localBaseUrl", baseUrl);
        data.put("questionsHtml", htmlQuestions);

        String body = tmpl.execute(data);

        tmpl = Mustache.compiler().escapeHTML(false).compile(layoutReader);
        data.put("title", "Does It Works??");
        data.put("localBaseUrl", baseUrl);

        data.put("body", body);
        String page = tmpl.execute(data);

        return page;
    }

    private DomContent setSurveyIcon(String issuer, String userId) {
        return img().withStyle("background-color: " + (issuer.equals(userId) ? "#FFD700;" : "#ccc;"));
    }

    private DecodedJWT extractAndVerifyJWTSignature(String jwt_token) throws UnsupportedEncodingException {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(jwt_token);
        } catch (JWTDecodeException exception) {
            //Invalid token
            //TODO:  log
            throw  exception;
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
            throw exception;
            //TODO: Log
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            throw exception;
            //TODO: Log

        }

        return jwt;
    }

    private DomContent generateMenu() {
        return div(attrs(".aui-connect-list-item-actions"),
                    button(attrs(".aui-dropdown2-trigger aui-button aui-dropdown2-trigger-arrowless"),
                        span(attrs(".aui-icon aui-icon-small aui-iconfont-more"))
                    ).withId("list-item-1-action-menu").attr("aria-owns","list-item-1").attr("aria-haspopup", "true").attr("data-no-focus", "true"),
                div(attrs(".aui-style-default aui-dropdown2 aui-connect-list-item-action"),
                        ul(attrs(".aui-list-truncate"),
                                li("Edit").withHref("#"),
                                li("Disable").withHref("#"))
                        ).withId("list-item-1")
        );
    }

    private int calculatePercentage(Survey survey, SurveyQuestion question) {

        OptionalInt totalVotes = survey.getQuestions().stream().mapToInt(q -> q.getAnswers().size()).reduce((x, y) -> x+y);

        int total = totalVotes.getAsInt();

        return  round((100*question.getAnswers().size())/total);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        service = retrofit.create(CapabilitiesService.class);

    }


}
