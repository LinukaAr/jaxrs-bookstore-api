/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapiweb;

/**
 *
 * @author LOQ
 */  
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import java.util.logging.Logger;
import java.util.logging.Level;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("com.mycompany.bookstoreapiweb.resource",
        "com.mycompany.bookstoreapiweb.exception",
        "com.mycompany.bookstoreapiweb.exception.mapper");

        // Register the LoggingFeature for logging requests and responses
        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
    }
}