package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
* This is a demo version. The database model H2 with pre-installed values is provided for demonstration.
* <p>
* !!Please note that the security concept is for demonstration purposes only and does not represent everyday protection.!!
* <p> *
* The application uses the annotation @Push to update in real time.
* Depending on the browser-settings or browser (FireFox),
* it can lead to an exception through "websocket" when pressing the Logout button.
* This is negligible in the demo version.
* The final configurations are usually only made when the final security system is implemented.
*
*  @author Daniel Altenburg
*  @version 1.0
*  @since 27.08.2023
*
*/
@Push
@PWA(name = "Taxi Controller", shortName = "Taxi App")
@SpringBootApplication
@Theme(value = "taxi-controller")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
