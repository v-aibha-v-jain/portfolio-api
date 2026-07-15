package com.example.portfolio;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RootController implements ErrorController {
    private final PortfolioService portfolioService;

    public RootController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping(value = { "/", "" })
    public ResponseEntity<JsonNode> getHelp() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("help"));
    }

    @RequestMapping("/error")
    public ResponseEntity<JsonNode> handleError() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("help"));
    }

    @RequestMapping(value = "/**")
    public ResponseEntity<JsonNode> fallbackAll(HttpServletRequest request) {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("help"));
    }
}