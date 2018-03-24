package com.valdemar.controller;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.valdemar.model.Installable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AddonController {


    @Value("${application.baseUrl}")
    private String baseUrl;

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
    public void installable(@RequestBody Installable body){
//https://developer.atlassian.com/server/hipchat/building-an-add-on-with-your-own-technology-stack/

        return;
    }


    @GetMapping(path= "/glance")
    public String glance(@RequestHeader HttpHeaders headers) throws FileNotFoundException {
//        https://developer.atlassian.com/server/hipchat/glances/
//        https://www.hipchat.com/docs/apiv2/glances?_ga=2.71212176.1416251405.1521659281-1852571517.1521309145

        return "{\n" +
                "  \"label\": {\n" +
                "    \"type\": \"html\",\n" +
                "    \"value\": \"<b>4</b> Repositories\"\n" +
                "  },\n" +
                "  \"status\": {\n" +
                "    \"type\": \"lozenge\",\n" +
                "    \"value\": {\n" +
                "        \"label\": \"LOCKED\",\n" +
                "        \"type\": \"current\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"metadata\": {\n" +
                "    \"isConfigured\": true\n" +
                "  }\n" +
                "}";

    }
        @GetMapping(path= "/configure")
    public String configure(@RequestHeader HttpHeaders headers) throws FileNotFoundException {


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

}
