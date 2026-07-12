package com.placement.platform.career.intelligence;

import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfidenceScoreCalculatorTest {

    private CareerIntelligenceProfileBuilder builder;
    private CareerIntelligenceProperties properties;

    @BeforeEach
    void setUp() {
        builder = new CareerIntelligenceProfileBuilder(null, null, null, null, null, null, null);
        properties = new CareerIntelligenceProperties();
    }

    @Test
    void testConfidenceScore_DefaultWeights() {
        // matchScore=90 (40%), placementReadiness=80 (25%), resumeScore=70 (20%), interviewScore=60 (15%)
        // expected score: (90*40 + 80*25 + 70*20 + 60*15) / 100 = (3600 + 2000 + 1400 + 900) / 100 = 7900 / 100 = 79
        int calculated = calculateConfidenceScore(90, 80, 70, 60);
        assertEquals(79, calculated);
    }

    @Test
    void testConfidenceScore_CustomWeights() {
        // Set custom weights
        properties.getConfidence().setMatchWeight(50);
        properties.getConfidence().setPlacementWeight(20);
        properties.getConfidence().setResumeWeight(20);
        properties.getConfidence().setInterviewWeight(10);

        // matchScore=90 (50%), placementReadiness=80 (20%), resumeScore=70 (20%), interviewScore=60 (10%)
        // expected score: (90*50 + 80*20 + 70*20 + 60*10) / 100 = (4500 + 1600 + 1400 + 600) / 100 = 8100 / 100 = 81
        int calculated = calculateConfidenceScore(90, 80, 70, 60);
        assertEquals(81, calculated);
    }

    @Test
    void testConfidenceScore_ZeroTotalWeight() {
        properties.getConfidence().setMatchWeight(0);
        properties.getConfidence().setPlacementWeight(0);
        properties.getConfidence().setResumeWeight(0);
        properties.getConfidence().setInterviewWeight(0);

        int calculated = calculateConfidenceScore(90, 80, 70, 60);
        assertEquals(0, calculated);
    }

    @Test
    void testConfidenceBand_Derivation() {
        // VERY_HIGH >= 85
        assertEquals(CareerConfidenceBand.VERY_HIGH, calculateConfidenceBand(85));
        assertEquals(CareerConfidenceBand.VERY_HIGH, calculateConfidenceBand(95));

        // HIGH >= 70
        assertEquals(CareerConfidenceBand.HIGH, calculateConfidenceBand(70));
        assertEquals(CareerConfidenceBand.HIGH, calculateConfidenceBand(84));

        // MEDIUM >= 50
        assertEquals(CareerConfidenceBand.MEDIUM, calculateConfidenceBand(50));
        assertEquals(CareerConfidenceBand.MEDIUM, calculateConfidenceBand(69));

        // LOW < 50
        assertEquals(CareerConfidenceBand.LOW, calculateConfidenceBand(49));
        assertEquals(CareerConfidenceBand.LOW, calculateConfidenceBand(10));
    }

    private int calculateConfidenceScore(int matchScore, int placementReadiness, int resumeScore, int interviewScore) {
        return builder.calculateConfidenceScore(matchScore, placementReadiness, resumeScore, interviewScore, properties);
    }

    private CareerConfidenceBand calculateConfidenceBand(int score) {
        return builder.calculateConfidenceBand(score);
    }
}
