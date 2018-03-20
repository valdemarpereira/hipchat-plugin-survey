package com.valdemar.controller;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(produces = "application/json")
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


    /*

     @GetMapping("/hello-world")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    The @ResponseBody annotation tells Spring MVC not to render a model into a view, but rather to write the returned object into the response body. It does this by using one of Spring’s message converters. Because Jackson 2 is in the classpath, this means that MappingJackson2HttpMessageConverter will handle the conversion of Greeting to JSON if the request’s Accept header specifies that JSON should be returned.
     */
}
