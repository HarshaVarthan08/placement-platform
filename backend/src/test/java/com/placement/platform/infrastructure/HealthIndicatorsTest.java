package com.placement.platform.infrastructure;

import com.placement.platform.career.config.CareerIntelligenceProperties;
import com.placement.platform.health.*;
import com.placement.platform.job.matching.JobMatchingEngine;
import com.placement.platform.job.service.source.JobSourceRegistry;
import com.placement.platform.premium.config.PremiumProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HealthIndicatorsTest {

    @Test
    public void testCustomDatabaseHealthIndicator_Up() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(anyString())).thenReturn(true);

        CustomDatabaseHealthIndicator indicator = new CustomDatabaseHealthIndicator(dataSource);
        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals("MySQL", health.getDetails().get("database"));
    }

    @Test
    public void testCustomDatabaseHealthIndicator_Down() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Connection failed"));

        CustomDatabaseHealthIndicator indicator = new CustomDatabaseHealthIndicator(dataSource);
        Health health = indicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    public void testCareerIntelligenceEngineHealthIndicator_Up() {
        CareerIntelligenceProperties properties = mock(CareerIntelligenceProperties.class);
        CareerIntelligenceProperties.Confidence confidence = mock(CareerIntelligenceProperties.Confidence.class);

        when(properties.getConfidence()).thenReturn(confidence);
        when(confidence.getMatchWeight()).thenReturn(40);
        when(confidence.getPlacementWeight()).thenReturn(25);
        when(confidence.getResumeWeight()).thenReturn(20);
        when(confidence.getInterviewWeight()).thenReturn(15);

        CareerIntelligenceEngineHealthIndicator indicator = new CareerIntelligenceEngineHealthIndicator(properties);
        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    public void testCareerIntelligenceEngineHealthIndicator_Down() {
        CareerIntelligenceProperties properties = mock(CareerIntelligenceProperties.class);
        CareerIntelligenceProperties.Confidence confidence = mock(CareerIntelligenceProperties.Confidence.class);

        when(properties.getConfidence()).thenReturn(confidence);
        when(confidence.getMatchWeight()).thenReturn(30);

        CareerIntelligenceEngineHealthIndicator indicator = new CareerIntelligenceEngineHealthIndicator(properties);
        Health health = indicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    public void testPremiumModuleHealthIndicator() {
        PremiumProperties properties = mock(PremiumProperties.class);
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLaunchQuarter()).thenReturn("Q4 2026");
        when(properties.isWaitlistEnabled()).thenReturn(true);

        PremiumModuleHealthIndicator indicator = new PremiumModuleHealthIndicator(properties);
        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(true, health.getDetails().get("enabled"));
    }

    @Test
    public void testJobSourceFrameworkHealthIndicator() {
        JobSourceRegistry registry = mock(JobSourceRegistry.class);
        when(registry.getAllSources()).thenReturn(Collections.emptyList());

        JobSourceFrameworkHealthIndicator indicator = new JobSourceFrameworkHealthIndicator(registry);
        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(0, health.getDetails().get("registeredSourcesCount"));
    }

    @Test
    public void testRecommendationEngineHealthIndicator() {
        JobMatchingEngine matchingEngine = mock(JobMatchingEngine.class);

        RecommendationEngineHealthIndicator indicator = new RecommendationEngineHealthIndicator(matchingEngine);
        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
    }
}
