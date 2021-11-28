package com.example.routes;

import com.example.beans.FileReaderBean;
import com.example.dto.FileMessageDto;
import com.example.dto.SaveFileDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;
import org.joda.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
public class SaveFileRoute extends RouteBuilder {
    @Inject
    FileReaderBean fileReaderBean;

    @Override
    public void configure() throws Exception {

        rest("/file-server")
                .post("/save-file")
                .description("save-file")
                .id(Routes.SAVE_FILE_ROUTE)
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON)
                .enableCORS(true)
                .responseMessage().code(201).responseModel(SaveFileDto.class).endResponseMessage()
                .param().name("file-name").type(RestParamType.query).dataType("string").example("camel-test").required(true).endParam()
                .type(FileMessageDto.class)
                .route()
                .routeId(Routes.SAVE_FILE_ROUTE)
                .routeGroup(Routes.SAVE_FILE_GROUP)
                .log("Request for save file with fileName ${in.header.file-name} with body ===> ${body}")
                .setHeader(Exchange.FILE_NAME, simple("${in.header.file-name}"))
                .process(exchange -> {
                    FileMessageDto messageDto = exchange.getMessage().getBody(FileMessageDto.class);
                    String body = String.format("DateCreated : %s, text: %s",LocalDateTime.now(),messageDto.getText());
                    exchange.getMessage().setBody(body);
                })
                .to("file:" + fileReaderBean.getServerDirURI())
                .process(exchange -> {
                    SaveFileDto fileDto = new SaveFileDto();
                    fileDto.setMessage("File created successfully");
                    exchange.getIn().setBody(fileDto);
                })
                .marshal().json(JsonLibrary.Jackson)
                .log("Response for save file ===> ${in.body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
    }
}
