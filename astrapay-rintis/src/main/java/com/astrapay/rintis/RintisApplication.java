package com.astrapay.rintis;

import com.astrapay.rintis.config.GreetClient;
import com.astrapay.rintis.config.GreetServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RintisApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RintisApplication.class, args);
        //TCPClient tcpClient = new TCPClient();
        //tcpClient.connect();
        //GreetServer greetServer = new GreetServer();
        //greetServer.start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RintisApplication.class);
    }

}
