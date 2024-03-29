package org.hello;

import com.awake.event.EventContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class HelloApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run(HelloApplication.class, args);
        System.out.println("hello world");
    }
}
