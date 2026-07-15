package com.example.portfolio;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class PortfolioService {
    private final ObjectMapper objectMapper;
    private final S3Client s3Client;

    private final String bucketName = "vaibhav-jain-portfolio";
    
    private JsonNode cachedRootNode;

    public PortfolioService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }
    
    @PostConstruct
    public synchronized void initCache() {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key("portfolio.json")
                    .build();

            try (InputStream inputStream = s3Client.getObject(getObjectRequest)) {
                this.cachedRootNode = objectMapper.readTree(inputStream);
                System.out.println("--> Cache synchronized with latest S3 data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load portfolio database from S3", e);
        }
    }

    public JsonNode getPortfolioSection(String sectionName) {
        if (cachedRootNode == null) {
            throw new IllegalStateException("Portfolio cache is not initialized.");
        }
        return cachedRootNode.get(sectionName);
    }
}