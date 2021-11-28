package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;

@Singleton
public class RouteDefinition  extends RouteBuilder {

        @ConfigProperty(name = "service.api.base-path", defaultValue = "/services/file")
        String basePath;

        @ConfigProperty(name = "service.log.pretty-print", defaultValue = "true")
        String prettyPrint;

        @ConfigProperty(name = "service.api.swagger-path")
        String swaggerPath;

        @Override
        public void configure() throws Exception {

            restConfiguration()
                    .contextPath(basePath)
                    .apiContextPath(swaggerPath)
                    .apiProperty("api.title", "Camel Quarkus file")
                    .apiProperty("api.version", "version1.0")
                    .apiProperty("api.cors", "true")
                    .bindingMode(RestBindingMode.json)
                    .enableCORS(true)
                    .dataFormatProperty("prettyPrint", prettyPrint);

        }

}
