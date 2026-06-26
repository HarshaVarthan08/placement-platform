package com.placement.platform.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.exception.GeminiApiException;
import com.placement.platform.exception.ResumeAnalysisException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiAIServiceImpl implements GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public GeminiAIServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ResumeAnalysisResponseDto analyzeResumeText(String resumeText) {
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("${GEMINI_API_KEY}")) {
            throw new GeminiApiException("Gemini API key is missing or not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        String prompt = "You are an expert ATS (Applicant Tracking System) and career coach. " +
                "Analyze the following resume text and provide a structured JSON response containing:\n" +
                "1. atsScore: an integer between 0 and 100 representing the ATS suitability score.\n" +
                "2. summary: a concise professional summary of the candidate.\n" +
                "3. strengths: a list of key professional strengths of the candidate.\n" +
                "4. weaknesses: a list of areas for improvement.\n" +
                "5. missingSkills: a list of important industry skills that are missing or could be added.\n" +
                "6. suggestions: a list of actionable suggestions to improve the resume.\n\n" +
                "Respond ONLY with a valid JSON object matching this schema:\n" +
                "{\n" +
                "  \"atsScore\": 82,\n" +
                "  \"summary\": \"...\",\n" +
                "  \"strengths\": [\"...\", \"...\"],\n" +
                "  \"weaknesses\": [\"...\", \"...\"],\n" +
                "  \"missingSkills\": [\"...\", \"...\"],\n" +
                "  \"suggestions\": [\"...\", \"...\"]\n" +
                "}\n\n" +
                "Do not include any markdown formatting, explanations, prefix or suffix. Only return the raw JSON object.\n\n" +
                "Resume Text:\n" +
                resumeText;

        // Build Payload
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> partsObj = new HashMap<>();
        partsObj.put("parts", Collections.singletonList(textPart));

        requestBody.put("contents", Collections.singletonList(partsObj));

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        requestBody.put("generationConfig", generationConfig);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, requestBody, String.class);
        } catch (Exception e) {
            throw new GeminiApiException("Failed to communicate with Gemini API: " + e.getMessage(), e);
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new GeminiApiException("Gemini API call failed with status: " + response.getStatusCode());
        }

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode candidates = rootNode.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    String rawText = parts.get(0).path("text").asText();
                    if (rawText != null && !rawText.trim().isEmpty()) {
                        String cleanedJson = cleanJsonString(rawText);
                        ResumeAnalysisResponseDto parsedDto = objectMapper.readValue(cleanedJson, ResumeAnalysisResponseDto.class);
                        validateResponse(parsedDto);
                        return new ResumeAnalysisResponseDto(
                                parsedDto.atsScore(),
                                parsedDto.summary(),
                                parsedDto.strengths(),
                                parsedDto.weaknesses(),
                                parsedDto.missingSkills(),
                                parsedDto.suggestions(),
                                java.time.LocalDateTime.now()
                        );
                    }
                }
            }
            throw new GeminiApiException("Failed to parse analysis: Gemini response candidates block was empty or malformed.");
        } catch (ResumeAnalysisException e) {
            throw e;
        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse Gemini API response: " + e.getMessage(), e);
        }
    }

    @Override
    public String getModelUsed() {
        return model;
    }

    private String cleanJsonString(String text) {
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        } else if (text.startsWith("```")) {
            text = text.substring(3);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }
        return text.trim();
    }

    private void validateResponse(ResumeAnalysisResponseDto dto) {
        if (dto == null) {
            throw new ResumeAnalysisException("Gemini returned null response");
        }
        if (dto.atsScore() == null) {
            throw new ResumeAnalysisException("Invalid analysis: atsScore is missing");
        }
        if (dto.summary() == null || dto.summary().trim().isEmpty()) {
            throw new ResumeAnalysisException("Invalid analysis: summary is missing or empty");
        }
        if (dto.strengths() == null || dto.strengths().isEmpty()) {
            throw new ResumeAnalysisException("Invalid analysis: strengths list is missing or empty");
        }
        if (dto.weaknesses() == null || dto.weaknesses().isEmpty()) {
            throw new ResumeAnalysisException("Invalid analysis: weaknesses list is missing or empty");
        }
        if (dto.missingSkills() == null || dto.missingSkills().isEmpty()) {
            throw new ResumeAnalysisException("Invalid analysis: missingSkills list is missing or empty");
        }
        if (dto.suggestions() == null || dto.suggestions().isEmpty()) {
            throw new ResumeAnalysisException("Invalid analysis: suggestions list is missing or empty");
        }
    }
}
