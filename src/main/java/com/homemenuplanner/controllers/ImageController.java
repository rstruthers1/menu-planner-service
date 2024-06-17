package com.homemenuplanner.controllers;


import com.homemenuplanner.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;

    @Autowired
    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            File tempFile = File.createTempFile("upload-", null);
            file.transferTo(tempFile);
            s3Service.uploadFile(key, tempFile);
            Map<String, String> response = new HashMap<>();
            response.put("imageFileName", key);
            tempFile.delete();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{imageFileName}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable(name="imageFileName") String imageFileName) {
        InputStream imageStream = s3Service.downloadFile(imageFileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + imageFileName)
                .body(new InputStreamResource(imageStream));
    }
}
