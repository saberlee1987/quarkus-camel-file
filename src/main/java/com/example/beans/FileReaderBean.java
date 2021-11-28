package com.example.beans;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
@Slf4j
@Named(value = "fileReader")
public class FileReaderBean {

    public void listFiles(Exchange exchange) throws URISyntaxException {
        File serverFiles = new File(getServerDirURI());
        if (serverFiles.exists()) {
            List<String> fileNames = new ArrayList<>();
            for (File file : Objects.requireNonNull(serverFiles.listFiles())) {
                fileNames.add(file.getName());
            }
            if (!fileNames.isEmpty()) {
                log.info("setting list of files to exchange");
                exchange.getMessage().setBody(fileNames);
            } else {
                log.error("no files found");
            }
        } else {
            log.error("No File created yet");
        }
    }

    public String getServerDirURI() throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(FileReaderBean.class.getResource("/")).toURI()).getParent()
                + "/camel-file-rest-dir";
    }
}
