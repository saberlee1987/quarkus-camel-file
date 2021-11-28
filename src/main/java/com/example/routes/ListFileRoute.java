package com.example.routes;

import com.example.dto.FileMessageDto;
import com.example.dto.ListFilesDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
public class ListFileRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        rest("/file-server")
                .get("/list-file")
                .description("list-file")
                .id(Routes.LIST_FILE_ROUTE)
                .produces(MediaType.APPLICATION_JSON)
                .enableCORS(true)
                .responseMessage().code(200).responseModel(ListFilesDto.class).endResponseMessage()
                .type(FileMessageDto.class)
                .route()
                .routeId(Routes.LIST_FILE_ROUTE)
                .routeGroup(Routes.LIST_FILE_GROUP)
                .log("Request for get list file ")
                .to("bean:fileReader?method=listFiles")
                .process(exchange -> {
                    List<String> listFiles = (List<String>) exchange.getMessage().getBody();
                    ListFilesDto filesDto = new ListFilesDto();
                    if (listFiles!=null){
                        filesDto.setListFiles(listFiles);
                    }
                     exchange.getMessage().setBody(filesDto);
                })
                .marshal().json(JsonLibrary.Jackson)
                .log("Response for get list files ===> ${in.body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}
