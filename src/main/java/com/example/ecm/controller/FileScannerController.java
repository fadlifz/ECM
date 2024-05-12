package com.example.ecm.controller;

import com.example.ecm.request.AllRequest;
import com.example.ecm.response.AllResponse;
import com.example.ecm.service.ExtractionService;

import net.sourceforge.tess4j.TesseractException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileScannerController {

    @Autowired
    private ExtractionService extractionService;

    @PostMapping("/savePdfText")
    ResponseEntity<Map<String, Object>> scanFile(@RequestBody AllRequest allRequest)
            throws Exception {
        AllResponse text = extractionService.extract(allRequest);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("result", text);
        return new ResponseEntity<>(jsonMap, HttpStatus.OK);
    }
}
