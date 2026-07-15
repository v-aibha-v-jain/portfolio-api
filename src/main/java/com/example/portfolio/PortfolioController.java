package com.example.portfolio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api")
public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping(value = { "/", "" })
    public ResponseEntity<JsonNode> Land() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("help"));
    }

    @GetMapping("/exp")
    public ResponseEntity<JsonNode> getExperience() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("experience"));
    }

    @GetMapping("/about")
    public ResponseEntity<JsonNode> getAbout() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("basics"));
    }

    @GetMapping("/skills")
    public ResponseEntity<JsonNode> getSkills() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("skills"));
    }

    @GetMapping("/education")
    public ResponseEntity<JsonNode> getEducation() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("education"));
    }

    @GetMapping("/projects")
    public ResponseEntity<JsonNode> getProjects() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("projects"));
    }

    @GetMapping("/certifications")
    public ResponseEntity<JsonNode> getCertifications() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("certifications"));
    }

    @GetMapping("/achievements")
    public ResponseEntity<JsonNode> getAchievements() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("achievements"));
    }

    @GetMapping("/socials")
    public ResponseEntity<JsonNode> getSocials() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("social_links"));
    }

    @GetMapping("/help")
    public ResponseEntity<JsonNode> getHelp() {
        return ResponseEntity.ok(portfolioService.getPortfolioSection("help"));
    }
}