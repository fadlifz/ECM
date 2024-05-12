package com.example.ecm.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import com.example.ecm.request.AllRequest;
import com.example.ecm.response.AllResponse;
import java.io.File;
import java.io.FileWriter;

@Service
public class ExtractionService {

    public static final DateTimeFormatter yyMMddHHmmssSSFormat = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    public AllResponse extract(AllRequest allRequest) throws IOException {

        File newTxtFile = new File("textFromImage_" + yyMMddHHmmssSSFormat.format(LocalDateTime.now()) + ".txt");
        FileWriter writer = new FileWriter(newTxtFile);
        writer.write(allRequest.getExtractedText());
        writer.close();

        return AllResponse.builder()
                .txtFile("successfully generated txt file " + newTxtFile)
                .build();
    }
}
