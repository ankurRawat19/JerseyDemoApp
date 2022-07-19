package com.example.jerseyDemo.config;

import com.example.jerseyDemo.model.JacketDemo;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacketDemo.class);
    }
}
