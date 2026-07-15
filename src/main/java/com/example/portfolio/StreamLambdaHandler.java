package com.example.portfolio;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(PortfolioApplication.class);
        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Could not initialize Spring Boot framework", e);
        }
    }

    // @Override
    // public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
    //         throws IOException {
    //     handler.proxyStream(inputStream, outputStream, context);
    // }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        String rawPayload = new String(bytes, StandardCharsets.UTF_8);

        if (rawPayload.contains("Records") && rawPayload.contains("s3")) {
            context.getLogger().log("⚡ Secured Internal S3 Mutation Trigger Caught!");
            
            var appContext = WebApplicationContextUtils.getWebApplicationContext(handler.getServletContext());
            
            if (appContext != null) {
                PortfolioService portfolioService = appContext.getBean(PortfolioService.class);
                
                portfolioService.initCache();
                context.getLogger().log("In-Memory RAM Cache Refreshed Successfully via Native AWS Event.");
            }
            return;
        }

        handler.proxyStream(new java.io.ByteArrayInputStream(bytes), outputStream, context);
    }
}