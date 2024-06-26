package com.bookstore.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {
    @RequestMapping(value = "getimage/{image}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getImage(@PathVariable("image") String image) {
        try {
            Path filename = Paths.get("uploads", image);
            byte[] buffer = Files.readAllBytes(filename);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
