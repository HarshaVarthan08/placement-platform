package com.placement.platform.career.intelligence;

import com.placement.platform.entity.User;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.entity.Job;
import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CareerIntelligenceEngineTest {

    @Mock private CareerIntelligenceProfileBuilder builder;
    private CareerIntelligenceEngine engine;
    private CareerIntelligenceProperties properties;

    @BeforeEach
    void setUp() {
        properties = new CareerIntelligenceProperties();
        engine = new CareerIntelligenceEngine(builder, properties);
    }

    @Test
    void testEngineBuildProfileDelegation() {
        User user = new User();
        JobRecommendation recommendation = new JobRecommendation();
        Job job = new Job();

        CareerIntelligenceProfile mockProfile = mock(CareerIntelligenceProfile.class);
        when(builder.buildProfile(user, recommendation, job, properties)).thenReturn(mockProfile);

        CareerIntelligenceProfile profile = engine.buildProfile(user, recommendation, job);

        assertNotNull(profile);
        verify(builder, times(1)).buildProfile(user, recommendation, job, properties);
    }
}
