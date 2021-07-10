package com.twb;

import com.twb.employee.reactive.EmployeeReactiveWebClient;
import com.twb.employee.webflux.EmployeeWebClient;
import com.twb.hello.client.GreetingWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        GreetingWebClient gwc = new GreetingWebClient();
        System.out.println(gwc.getResult());

        logger.info("\nWeb Client Employee");
        EmployeeWebClient empClient = new EmployeeWebClient();
        empClient.consume();

        logger.info("\nReactive Client Employee");
        EmployeeReactiveWebClient emplReact = new EmployeeReactiveWebClient();
        emplReact.consume();
    }
}
