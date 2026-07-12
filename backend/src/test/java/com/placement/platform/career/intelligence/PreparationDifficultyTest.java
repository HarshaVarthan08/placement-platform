package com.placement.platform.career.intelligence;

import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreparationDifficultyTest {

    private CareerIntelligenceProfileBuilder builder;
    private CareerIntelligenceProperties properties;

    @BeforeEach
    void setUp() {
        builder = new CareerIntelligenceProfileBuilder(null, null, null, null, null, null, null);
        properties = new CareerIntelligenceProperties();
    }

    @Test
    void testDifficultyRanges_DefaultProperties() {
        // VERY_HIGH: missing skills > 10
        assertEquals(PreparationDifficulty.VERY_HIGH, calculateDifficulty(11));
        
        // HIGH: missing skills 7-10
        assertEquals(PreparationDifficulty.HIGH, calculateDifficulty(10));
        assertEquals(PreparationDifficulty.HIGH, calculateDifficulty(7));
        
        // MEDIUM: missing skills 4-6
        assertEquals(PreparationDifficulty.MEDIUM, calculateDifficulty(6));
        assertEquals(PreparationDifficulty.MEDIUM, calculateDifficulty(4));
        
        // LOW: missing skills 0-3
        assertEquals(PreparationDifficulty.LOW, calculateDifficulty(3));
        assertEquals(PreparationDifficulty.LOW, calculateDifficulty(0));
    }

    @Test
    void testDifficultyRanges_CustomProperties() {
        properties.getDifficulty().setVeryHighThreshold(15);
        properties.getDifficulty().setHighThreshold(10);
        properties.getDifficulty().setMediumThreshold(5);

        assertEquals(PreparationDifficulty.VERY_HIGH, calculateDifficulty(16));
        assertEquals(PreparationDifficulty.HIGH, calculateDifficulty(12));
        assertEquals(PreparationDifficulty.MEDIUM, calculateDifficulty(7));
        assertEquals(PreparationDifficulty.LOW, calculateDifficulty(3));
    }

    private PreparationDifficulty calculateDifficulty(int missingSkillsCount) {
        return builder.calculateDifficulty(missingSkillsCount, properties);
    }
}
