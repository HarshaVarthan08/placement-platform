package com.placement.platform.job.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.job.matching.ScoreBreakdown;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class ScoreBreakdownConverter implements AttributeConverter<ScoreBreakdown, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScoreBreakdownConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ScoreBreakdown attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing ScoreBreakdown to JSON string", e);
            return null;
        }
    }

    @Override
    public ScoreBreakdown convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, ScoreBreakdown.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing JSON string to ScoreBreakdown", e);
            return null;
        }
    }
}
