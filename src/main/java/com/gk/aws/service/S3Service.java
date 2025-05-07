package com.gk.aws.service;

import com.gk.aws.util.OutputStreamAsyncResponseTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3AsyncClient s3AsyncClient;
    private final ExecutorService executorService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public CompletableFuture<PutObjectResponse> uploadFile(String key, InputStream inputStream, long contentLength){
        PutObjectRequest putObjectRequest=PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CompletableFuture<PutObjectResponse> future =
                s3AsyncClient.putObject(putObjectRequest,
                        AsyncRequestBody.fromInputStream(inputStream, contentLength, executorService));

        return future.whenComplete((resp, ex) -> {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<GetObjectResponse> downloadFileToStream(String key, OutputStream outputStream) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3AsyncClient.getObject(getRequest, new OutputStreamAsyncResponseTransformer<>(outputStream));
    }

    public CompletableFuture<DeleteObjectResponse> deleteFile(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3AsyncClient.deleteObject(deleteRequest);
    }

    public void shutdown() {
        executorService.shutdown();
    }


}
