package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import static spark.Spark.*;
/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            res.type("application/xml");
            Body body = new Body
                    .Builder("The Robots are coming! Head for the hills!")
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            return twiml.toXml();
        });
    }

}
