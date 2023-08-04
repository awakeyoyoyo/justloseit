package com.awake.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans({@ComponentScan("com.awake.net")})
public class GameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class, args);

    }

}
