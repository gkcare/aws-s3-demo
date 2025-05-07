package com.gk.aws.controller;

import com.gk.aws.service.S3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3Service.uploadFile(file.getOriginalFilename(), file.getInputStream(),file.getSize())
                .thenApply(res ->ResponseEntity.ok("Uploaded: "+res.eTag()));
    }

    @GetMapping("/download")
    public void download(@RequestParam String key, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + key + "\"");

        CompletableFuture<GetObjectResponse> future = s3Service.downloadFileToStream(key, response.getOutputStream());

        // block until complete for streaming to finish
        future.join();
    }

    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<String>> delete(@RequestParam("key") String key) {
        return s3Service.deleteFile(key)
                .thenApply(response -> ResponseEntity.ok("Deleted: " + key))
                .exceptionally(ex -> ResponseEntity.internalServerError().body("Error: " + ex.getMessage()));
    }

}
