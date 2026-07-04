package com.placement.platform.job.matching;

import com.placement.platform.job.entity.*;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JobMatchingEngineTest {

    private JobMatchingEngine matchingEngine;
    private ScoreAggregator scoreAggregator;
    private MatchingPipeline pipeline;

    private CandidateIntelligenceProfile candidate;
    private Job job;

    @BeforeEach
    void setUp() {
        scoreAggregator = new ScoreAggregator();
        // Set weights via Reflection since they are @Value annotated in Spring
        ReflectionTestUtils.setField(scoreAggregator, "skillsWeight", 35);
        ReflectionTestUtils.setField(scoreAggregator, "roleWeight", 20);
        ReflectionTestUtils.setField(scoreAggregator, "eligibilityWeight", 15);
        ReflectionTestUtils.setField(scoreAggregator, "resumeWeight", 10);
        ReflectionTestUtils.setField(scoreAggregator, "experienceWeight", 10);
        ReflectionTestUtils.setField(scoreAggregator, "educationWeight", 5);
        ReflectionTestUtils.setField(scoreAggregator, "locationWeight", 5);

        pipeline = new MatchingPipeline(scoreAggregator);
        matchingEngine = new JobMatchingEngine(pipeline);

        // Define Candidate Profile
        candidate = new CandidateIntelligenceProfile(
                1L,
                List.of("Software Engineer", "Developer"),
                List.of("Google", "Microsoft"),
                List.of("Java", "Spring Boot", "SQL"),
                2, // experience in years
                "B.Tech in Computer Science from IIT Delhi",
                new BigDecimal("9.00"),
                85, // ATS
                List.of("Java", "Spring Boot", "SQL"),
                90, // Interview Technical
                85, // Communication
                80, // Confidence
                95, // Readiness
                List.of("Google", "Microsoft"),
                List.of("Bangalore", "Hyderabad")
        );

        // Define Job
        job = new Job();
        job.setId(10L);
        job.setCompany("Google");
        job.setTitle("Software Engineer");
        job.setNormalizedRole("software engineer");
        job.setDescription("Looking for a Software Engineer with experience in Java, Spring Boot, and Docker. Master's in Computer Science preferred.");
        job.setMinimumExperience(2);
        job.setMinimumCGPA(new BigDecimal("8.00"));

        JobSkill js1 = new JobSkill();
        js1.setSkillName("Java");
        js1.setPreferred(false); // required

        JobSkill js2 = new JobSkill();
        js2.setSkillName("Spring Boot");
        js2.setPreferred(false); // required

        JobSkill js3 = new JobSkill();
        js3.setSkillName("Docker");
        js3.setPreferred(true); // preferred

        job.setSkills(new HashSet<>(List.of(js1, js2, js3)));

        JobLocation loc = new JobLocation();
        loc.setLocation("Bangalore");
        job.setLocations(new HashSet<>(List.of(loc)));
    }

    @Test
    void testIndividualStrategies() {
        // 1. Skill Match (Candidate matches Java and Spring Boot, misses Docker)
        SkillMatchStrategy skillStrategy = new SkillMatchStrategy();
        MatchComponentResult skillRes = skillStrategy.evaluate(candidate, job);
        assertEquals("Skills Match", skillRes.componentName());
        // Required: 2/2 matched. Preferred: 0/1 matched.
        // scoreVal = (2/2) * 80 + (0/1) * 20 = 80.
        assertEquals(80, skillRes.score());
        assertTrue(skillRes.matchedItems().contains("Java"));
        assertTrue(skillRes.missingItems().contains("Docker (Preferred)"));

        // 2. Role Match (Candidate preferred roles has "Software Engineer" matching job normalizedRole exactly)
        RoleMatchStrategy roleStrategy = new RoleMatchStrategy();
        MatchComponentResult roleRes = roleStrategy.evaluate(candidate, job);
        assertEquals(100, roleRes.score());

        // 3. Eligibility Match (Candidate CGPA = 9.00 >= 8.00, and candidate is eligible for Google)
        EligibilityMatchStrategy eligibilityStrategy = new EligibilityMatchStrategy();
        MatchComponentResult eligibilityRes = eligibilityStrategy.evaluate(candidate, job);
        assertEquals(100, eligibilityRes.score());

        // 4. Resume Match (Candidate resume ATS score is 85)
        ResumeMatchStrategy resumeStrategy = new ResumeMatchStrategy();
        MatchComponentResult resumeRes = resumeStrategy.evaluate(candidate, job);
        assertEquals(85, resumeRes.score());

        // 5. Experience Match (Candidate has 2 years >= required 2 years)
        ExperienceMatchStrategy experienceStrategy = new ExperienceMatchStrategy();
        MatchComponentResult experienceRes = experienceStrategy.evaluate(candidate, job);
        assertEquals(100, experienceRes.score());

        // 6. Education Match (Job description mentions Master's, candidate has B.Tech -> score should deduct 15 points)
        EducationMatchStrategy educationStrategy = new EducationMatchStrategy();
        MatchComponentResult educationRes = educationStrategy.evaluate(candidate, job);
        assertEquals(85, educationRes.score()); // 100 - 15 = 85

        // 7. Location Match (Candidate preferred Bangalore matches job location Bangalore exactly)
        LocationMatchStrategy locationStrategy = new LocationMatchStrategy();
        MatchComponentResult locationRes = locationStrategy.evaluate(candidate, job);
        assertEquals(100, locationRes.score());
    }

    @Test
    void testScoreAggregator_Success() {
        List<MatchComponentResult> results = List.of(
                new MatchComponentResult("Skills Match", 80, 100, "", List.of(), List.of()),
                new MatchComponentResult("Role Match", 100, 100, "", List.of(), List.of()),
                new MatchComponentResult("Eligibility Match", 100, 100, "", List.of(), List.of()),
                new MatchComponentResult("Resume Match", 85, 100, "", List.of(), List.of()),
                new MatchComponentResult("Experience Match", 100, 100, "", List.of(), List.of()),
                new MatchComponentResult("Education Match", 85, 100, "", List.of(), List.of()),
                new MatchComponentResult("Location Match", 100, 100, "", List.of(), List.of())
        );

        ScoreBreakdown breakdown = scoreAggregator.aggregate(results);
        // Weighted Score:
        // (80*35 + 100*20 + 100*15 + 85*10 + 100*10 + 85*5 + 100*5) / 100
        // = (2800 + 2000 + 1500 + 850 + 1000 + 425 + 500) / 100
        // = 9075 / 100 = 91 (rounded)
        assertEquals(91, breakdown.totalScore());
        assertEquals(80, breakdown.skillScore());
        assertEquals(100, breakdown.roleScore());
        assertEquals(85, breakdown.resumeScore());

        // Confidence calculation (profile filled + resume analyzed + interview completed = 30 + 35 + 35 = 100)
        Integer confidence = scoreAggregator.calculateConfidenceScore(candidate);
        assertEquals(100, confidence);
    }

    @Test
    void testMatchingEngine_StatelessProcess() {
        List<RecommendationResult> results = matchingEngine.evaluateJobs(candidate, List.of(job));
        assertEquals(1, results.size());
        RecommendationResult res = results.get(0);
        assertEquals(10L, res.job().getId());
        assertEquals(91, res.finalMatchScore());
        assertEquals(100, res.confidenceScore());
        assertEquals(2, res.matchedSkillCount());
        assertEquals(2, res.totalRequiredSkills());
        assertEquals(100.0, res.skillMatchPercentage());
    }
}
