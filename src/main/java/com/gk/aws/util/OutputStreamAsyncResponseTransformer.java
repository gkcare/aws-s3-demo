package com.gk.aws.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public class OutputStreamAsyncResponseTransformer<T extends SdkResponse> implements AsyncResponseTransformer<T, T> {

    private final OutputStream outputStream;
    private final CompletableFuture<T> resultFuture = new CompletableFuture<>();
    private T sdkResponse;

    public OutputStreamAsyncResponseTransformer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public CompletableFuture<T> prepare() {
        return resultFuture;
    }

    @Override
    public void onResponse(T response) {
        this.sdkResponse = response;
    }

    @Override
    public void onStream(SdkPublisher<ByteBuffer> publisher) {
        publisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer byteBuffer) {
                try {
                    byte[] buffer = new byte[byteBuffer.remaining()];
                    byteBuffer.get(buffer);
                    outputStream.write(buffer);
                } catch (Exception e) {
                    resultFuture.completeExceptionally(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                resultFuture.completeExceptionally(throwable);
            }

            @Override
            public void onComplete() {
                try {
                    outputStream.flush();
                    outputStream.close();
                    resultFuture.complete(sdkResponse);
                } catch (Exception e) {
                    resultFuture.completeExceptionally(e);
                }
            }
        });
    }

    @Override
    public void exceptionOccurred(Throwable error) {
        resultFuture.completeExceptionally(error);
    }
}
