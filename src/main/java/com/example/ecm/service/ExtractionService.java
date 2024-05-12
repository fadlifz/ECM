package com.example.ecm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import com.example.ecm.request.AllRequest;
import com.example.ecm.response.AllResponse;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import java.awt.image.BufferedImage;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

@Service
public class ExtractionService {

    public static final DateTimeFormatter yyMMddHHmmssSSFormat = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    public AllResponse extract(AllRequest allRequest) throws Exception, IOException {

        String[] strings = allRequest.getFile().split(",");
        String extension;
        switch (strings[0]) {// check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default:// should write cases for more images types
                extension = "jpg";
                break;
        }
        // convert base64 string to binary data
        BufferedImage image = null;
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        image = ImageIO.read(bis);
        bis.close();

        // write the image to a pdf file
        File outputImageFile = new File(
                "image_" + yyMMddHHmmssSSFormat.format(LocalDateTime.now()) + "." + extension);
        ImageIO.write(image, extension, outputImageFile);
        String outputPdfFile = "output_" + yyMMddHHmmssSSFormat.format(LocalDateTime.now()) + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(new File(outputPdfFile)));
        document.open();
        document.newPage();
        Image readImage = Image.getInstance(new File(outputImageFile.getName()).getAbsolutePath());
        readImage.setAbsolutePosition(0, 0);
        readImage.setBorderWidth(0);
        readImage.scaleAbsolute(PageSize.A4);
        document.add(readImage);
        document.close();
        outputImageFile.delete();

        File newTxtFile = new File("textFromImage_" + yyMMddHHmmssSSFormat.format(LocalDateTime.now()) + ".txt");
        FileWriter writer = new FileWriter(newTxtFile);
        writer.write(allRequest.getExtractedText());
        writer.close();

        return AllResponse.builder()
                .txtFile("successfully generated txt file " + newTxtFile)
                .pdfFile("successfully generated pdf file " + outputPdfFile)
                .build();
    }
}
