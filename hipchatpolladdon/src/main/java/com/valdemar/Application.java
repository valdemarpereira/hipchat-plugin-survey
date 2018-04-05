package com.valdemar;

import com.valdemar.service.TokenStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {

    public static void main(String... args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TokenStore tokenStore(){
        return new TokenStore();
    }
}
