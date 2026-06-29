package com.placement.platform.service;

import com.placement.platform.dto.*;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.mapper.InterviewMapper;
import com.placement.platform.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final UserRepository userRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final InterviewProfileRepository interviewProfileRepository;
    private final InterviewMapper interviewMapper;

    private final GlobalQuestionRepository globalQuestionRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewQuestionExposureRepository interviewQuestionExposureRepository;
    private final InterviewSessionQuestionRepository interviewSessionQuestionRepository;
    private final InterviewQuestionPoolRepository interviewQuestionPoolRepository;

    @Value("${application.interview.cooldown-hours:12}")
    private int cooldownHours;

    @Value("${application.interview.learning.global-ratio:60}")
    private int globalRatio;

    @Value("${application.interview.learning.personalized-ratio:40}")
    private int personalizedRatio;

    @Value("${application.interview.default-total-questions:5}")
    private int defaultTotalQuestions;

    @Value("${application.interview.learning.questions:10}")
    private int learningQuestionsCount;

    @Value("${application.interview.mock.questions:15}")
    private int mockQuestionsCount;

    public InterviewServiceImpl(
            UserRepository userRepository,
            InterviewSessionRepository interviewSessionRepository,
            InterviewProfileRepository interviewProfileRepository,
            InterviewMapper interviewMapper,
            GlobalQuestionRepository globalQuestionRepository,
            InterviewQuestionRepository interviewQuestionRepository,
            InterviewQuestionExposureRepository interviewQuestionExposureRepository,
            InterviewSessionQuestionRepository interviewSessionQuestionRepository,
            InterviewQuestionPoolRepository interviewQuestionPoolRepository) {
        this.userRepository = userRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.interviewProfileRepository = interviewProfileRepository;
        this.interviewMapper = interviewMapper;
        this.globalQuestionRepository = globalQuestionRepository;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.interviewQuestionExposureRepository = interviewQuestionExposureRepository;
        this.interviewSessionQuestionRepository = interviewSessionQuestionRepository;
        this.interviewQuestionPoolRepository = interviewQuestionPoolRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewAvailabilityResponseDto checkAvailability() {
        User user = getAuthenticatedUser();
        Optional<InterviewSession> latestMockSessionOpt = interviewSessionRepository
                .findFirstByUserAndModeOrderByStartedAtDesc(user, InterviewMode.MOCK);

        if (latestMockSessionOpt.isEmpty()) {
            return new InterviewAvailabilityResponseDto(true, null, 0, 0);
        }

        InterviewSession latestSession = latestMockSessionOpt.get();

        // If the latest mock session is still in progress, the user cannot start a new one.
        if (latestSession.getStatus() == InterviewStatus.IN_PROGRESS) {
            return new InterviewAvailabilityResponseDto(false, null, 0, 0);
        }

        LocalDateTime cooldownUntil = latestSession.getCooldownUntil();

        if (cooldownUntil == null || LocalDateTime.now().isAfter(cooldownUntil)) {
            return new InterviewAvailabilityResponseDto(true, null, 0, 0);
        }

        Duration duration = Duration.between(LocalDateTime.now(), cooldownUntil);
        long remainingHours = duration.toHours();
        long remainingMinutes = duration.toMinutes() % 60;

        if (remainingHours < 0) remainingHours = 0;
        if (remainingMinutes < 0) remainingMinutes = 0;

        return new InterviewAvailabilityResponseDto(false, cooldownUntil, remainingHours, remainingMinutes);
    }

    @Override
    @Transactional
    public InterviewSessionResponseDto startInterview(StartInterviewRequestDto request) {
        User user = getAuthenticatedUser();

        // Prevent concurrent active sessions regardless of mode
        boolean hasActiveSession = interviewSessionRepository.existsByUserAndStatus(user, InterviewStatus.IN_PROGRESS);
        if (hasActiveSession) {
            throw new InterviewCooldownException("You already have an active interview session in progress. Please complete or cancel it first.");
        }

        if (request.mode() == InterviewMode.MOCK) {
            InterviewAvailabilityResponseDto availability = checkAvailability();
            if (!availability.available()) {
                if (availability.nextAvailableAt() == null) {
                    throw new InterviewCooldownException("You already have an active mock interview session in progress.");
                } else {
                    throw new InterviewCooldownException("Mock interview is on cooldown until " + availability.nextAvailableAt() 
                            + ". Remaining: " + availability.remainingHours() + " hours, " + availability.remainingMinutes() + " minutes.");
                }
            }
        }

        // Retrieve the current profile version used for lazy regeneration
        int profileVersion = interviewProfileRepository.findByUser(user)
                .map(InterviewProfile::getProfileVersion)
                .orElse(1);

        InterviewSession session = new InterviewSession();
        session.setUser(user);
        session.setMode(request.mode());
        session.setStatus(InterviewStatus.IN_PROGRESS);
        session.setProfileVersionUsed(profileVersion);
        session.setCurrentQuestionIndex(0);

        // Fetch active question pool for user
        InterviewQuestionPool activePool = interviewQuestionPoolRepository
                .findByUserAndStatus(user, QuestionPoolStatus.ACTIVE)
                .orElseThrow(() -> new QuestionPoolGenerationException("Active personalized question pool not found. Please generate a question pool first."));

        List<InterviewQuestion> personalizedCandidates = interviewQuestionRepository.findByQuestionPool(activePool);

        List<InterviewSessionQuestion> sessionQuestions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Load existing exposures for user and mode
        List<InterviewQuestionExposure> exposures = interviewQuestionExposureRepository.findByUserAndMode(user, request.mode());
        Map<Long, InterviewQuestionExposure> pExposureMap = new HashMap<>();
        Map<Long, InterviewQuestionExposure> gExposureMap = new HashMap<>();
        for (InterviewQuestionExposure exp : exposures) {
            if (exp.getInterviewQuestion() != null) {
                pExposureMap.put(exp.getInterviewQuestion().getId(), exp);
            }
            if (exp.getGlobalQuestion() != null) {
                gExposureMap.put(exp.getGlobalQuestion().getId(), exp);
            }
        }

        if (request.mode() == InterviewMode.LEARNING) {
            int totalCount = learningQuestionsCount;
            session.setTotalQuestions(totalCount);
            session.setQuestionPool(activePool);

            int globalCount = (int) Math.round(totalCount * 0.6);
            int personalizedCount = totalCount - globalCount;

            // Fetch global questions
            List<GlobalQuestion> globalCandidates = globalQuestionRepository.findAll();
            if (globalCandidates.size() < globalCount) {
                throw new QuestionPoolGenerationException("Not enough global questions available (found " + globalCandidates.size() + ", required " + globalCount + ").");
            }
            if (personalizedCandidates.size() < personalizedCount) {
                throw new QuestionPoolGenerationException("Not enough personalized questions available (found " + personalizedCandidates.size() + ", required " + personalizedCount + ").");
            }

            // Wrap and sort global questions
            List<QuestionCandidate<GlobalQuestion>> gWrapped = new ArrayList<>();
            for (GlobalQuestion gq : globalCandidates) {
                gWrapped.add(new QuestionCandidate<>(gq, gExposureMap.get(gq.getId())));
            }
            gWrapped.sort(getCandidateComparator());
            List<GlobalQuestion> selectedGlobal = gWrapped.stream()
                    .limit(globalCount)
                    .map(QuestionCandidate::getQuestion)
                    .collect(Collectors.toList());

            // Wrap and sort personalized questions
            List<QuestionCandidate<InterviewQuestion>> pWrapped = new ArrayList<>();
            for (InterviewQuestion iq : personalizedCandidates) {
                pWrapped.add(new QuestionCandidate<>(iq, pExposureMap.get(iq.getId())));
            }
            pWrapped.sort(getCandidateComparator());
            List<InterviewQuestion> selectedPersonalized = pWrapped.stream()
                    .limit(personalizedCount)
                    .map(QuestionCandidate::getQuestion)
                    .collect(Collectors.toList());

            // Save session first to obtain ID
            InterviewSession savedSession = interviewSessionRepository.save(session);

            int displayOrder = 1;
            for (GlobalQuestion gq : selectedGlobal) {
                InterviewSessionQuestion sq = new InterviewSessionQuestion();
                sq.setInterviewSession(savedSession);
                sq.setGlobalQuestion(gq);
                sq.setQuestionSource(QuestionSource.GLOBAL);
                sq.setDisplayOrder(displayOrder++);
                sq.setStatus(SessionQuestionStatus.NOT_STARTED);
                sq.setQuestionText(gq.getQuestion());
                sq.setTopic(gq.getTopic());
                sq.setCategory(null);
                sq.setDifficulty(gq.getDifficulty());
                sessionQuestions.add(sq);
            }

            for (InterviewQuestion iq : selectedPersonalized) {
                InterviewSessionQuestion sq = new InterviewSessionQuestion();
                sq.setInterviewSession(savedSession);
                sq.setPersonalizedQuestion(iq);
                sq.setQuestionSource(QuestionSource.PERSONALIZED);
                sq.setDisplayOrder(displayOrder++);
                sq.setStatus(SessionQuestionStatus.NOT_STARTED);
                sq.setQuestionText(iq.getQuestion());
                sq.setTopic(iq.getTopic());
                sq.setCategory(iq.getCategory());
                sq.setDifficulty(iq.getDifficulty());
                sessionQuestions.add(sq);
            }

            // The first question (displayOrder = 1) is set to CURRENT
            if (!sessionQuestions.isEmpty()) {
                InterviewSessionQuestion firstQuestion = sessionQuestions.get(0);
                firstQuestion.setStatus(SessionQuestionStatus.CURRENT);
                firstQuestion.setQuestionStartedAt(now);
            }

            interviewSessionQuestionRepository.saveAll(sessionQuestions);

            // Record/update exposures
            for (GlobalQuestion gq : selectedGlobal) {
                InterviewQuestionExposure exp = interviewQuestionExposureRepository
                        .findByUserAndGlobalQuestionAndMode(user, gq, InterviewMode.LEARNING)
                        .orElseGet(() -> {
                            InterviewQuestionExposure newExp = new InterviewQuestionExposure();
                            newExp.setUser(user);
                            newExp.setGlobalQuestion(gq);
                            newExp.setMode(InterviewMode.LEARNING);
                            newExp.setTimesShown(0);
                            return newExp;
                        });
                exp.setTimesShown(exp.getTimesShown() + 1);
                exp.setLastPracticedAt(now);
                interviewQuestionExposureRepository.save(exp);
            }

            for (InterviewQuestion iq : selectedPersonalized) {
                InterviewQuestionExposure exp = interviewQuestionExposureRepository
                        .findByUserAndInterviewQuestionAndMode(user, iq, InterviewMode.LEARNING)
                        .orElseGet(() -> {
                            InterviewQuestionExposure newExp = new InterviewQuestionExposure();
                            newExp.setUser(user);
                            newExp.setInterviewQuestion(iq);
                            newExp.setMode(InterviewMode.LEARNING);
                            newExp.setTimesShown(0);
                            return newExp;
                        });
                exp.setTimesShown(exp.getTimesShown() + 1);
                exp.setLastPracticedAt(now);
                interviewQuestionExposureRepository.save(exp);
            }

            return interviewMapper.toDto(savedSession);

        } else { // MOCK mode
            int totalCount = mockQuestionsCount;
            session.setTotalQuestions(totalCount);
            session.setQuestionPool(activePool);

            if (personalizedCandidates.size() < totalCount) {
                throw new QuestionPoolGenerationException("Not enough personalized questions available (found " + personalizedCandidates.size() + ", required " + totalCount + ").");
            }

            // Wrap and sort personalized questions
            List<QuestionCandidate<InterviewQuestion>> pWrapped = new ArrayList<>();
            for (InterviewQuestion iq : personalizedCandidates) {
                pWrapped.add(new QuestionCandidate<>(iq, pExposureMap.get(iq.getId())));
            }
            pWrapped.sort(getCandidateComparator());
            List<InterviewQuestion> selectedPersonalized = pWrapped.stream()
                    .limit(totalCount)
                    .map(QuestionCandidate::getQuestion)
                    .collect(Collectors.toList());

            // Order selected questions by Category: Projects -> Technical -> Scenario -> Behavioral -> HR
            selectedPersonalized.sort(Comparator.comparingInt(iq -> {
                QuestionCategory cat = iq.getCategory();
                if (cat == null) return 5;
                switch (cat) {
                    case PROJECT: return 0;
                    case TECHNICAL: return 1;
                    case SCENARIO: return 2;
                    case BEHAVIORAL: return 3;
                    case HR: return 4;
                    default: return 5;
                }
            }));

            // Save session first to obtain ID
            InterviewSession savedSession = interviewSessionRepository.save(session);

            int displayOrder = 1;
            for (InterviewQuestion iq : selectedPersonalized) {
                InterviewSessionQuestion sq = new InterviewSessionQuestion();
                sq.setInterviewSession(savedSession);
                sq.setPersonalizedQuestion(iq);
                sq.setQuestionSource(QuestionSource.PERSONALIZED);
                sq.setDisplayOrder(displayOrder++);
                sq.setStatus(SessionQuestionStatus.NOT_STARTED);
                sq.setQuestionText(iq.getQuestion());
                sq.setTopic(iq.getTopic());
                sq.setCategory(iq.getCategory());
                sq.setDifficulty(iq.getDifficulty());
                sessionQuestions.add(sq);
            }

            // The first question (displayOrder = 1) is set to CURRENT
            if (!sessionQuestions.isEmpty()) {
                InterviewSessionQuestion firstQuestion = sessionQuestions.get(0);
                firstQuestion.setStatus(SessionQuestionStatus.CURRENT);
                firstQuestion.setQuestionStartedAt(now);
            }

            interviewSessionQuestionRepository.saveAll(sessionQuestions);

            // Record/update exposures
            for (InterviewQuestion iq : selectedPersonalized) {
                InterviewQuestionExposure exp = interviewQuestionExposureRepository
                        .findByUserAndInterviewQuestionAndMode(user, iq, InterviewMode.MOCK)
                        .orElseGet(() -> {
                            InterviewQuestionExposure newExp = new InterviewQuestionExposure();
                            newExp.setUser(user);
                            newExp.setInterviewQuestion(iq);
                            newExp.setMode(InterviewMode.MOCK);
                            newExp.setTimesShown(0);
                            return newExp;
                        });
                exp.setTimesShown(exp.getTimesShown() + 1);
                exp.setLastPracticedAt(now);
                interviewQuestionExposureRepository.save(exp);
            }

            return interviewMapper.toDto(savedSession);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewQuestionResponseDto getCurrentQuestion() {
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findByUserAndStatus(user, InterviewStatus.IN_PROGRESS)
                .orElseThrow(() -> new InterviewSessionNotFoundException("No active interview session found. Please start an interview first."));

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session);
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found in this interview session.");
        }

        int index = session.getCurrentQuestionIndex();
        if (index < 0 || index >= questions.size()) {
            throw new ResourceNotFoundException("Current question index out of bounds.");
        }

        InterviewSessionQuestion current = questions.get(index);
        return new InterviewQuestionResponseDto(
                current.getId(),
                current.getQuestionText(),
                current.getTopic(),
                current.getDifficulty(),
                current.getCategory(),
                current.getDisplayOrder(),
                current.getQuestionSource(),
                current.getStatus()
        );
    }

    @Override
    @Transactional
    public void answerCurrentQuestion(SubmitAnswerRequestDto request) {
        if (request.answerType() == null) {
            throw new IllegalArgumentException("Answer type is required.");
        }
        if (request.answerType() == AnswerType.TEXT) {
            if (request.textAnswer() == null || request.textAnswer().trim().isEmpty()) {
                throw new IllegalArgumentException("Text answer must not be empty when answer type is TEXT.");
            }
        } else if (request.answerType() == AnswerType.AUDIO) {
            if (request.audioFilePath() == null || request.audioFilePath().trim().isEmpty()) {
                throw new IllegalArgumentException("Audio file path must not be empty when answer type is AUDIO.");
            }
        }

        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findByUserAndStatus(user, InterviewStatus.IN_PROGRESS)
                .orElseThrow(() -> new InterviewSessionNotFoundException("No active interview session found."));

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session);
        int index = session.getCurrentQuestionIndex();
        if (index < 0 || index >= questions.size()) {
            throw new ResourceNotFoundException("Current question index out of bounds.");
        }

        InterviewSessionQuestion current = questions.get(index);

        if (current.getStatus() == SessionQuestionStatus.ANSWERED) {
            throw new IllegalStateException("Question has already been answered.");
        }

        LocalDateTime answeredAt = LocalDateTime.now();
        current.setStatus(SessionQuestionStatus.ANSWERED);
        current.setAnswerType(request.answerType());
        if (request.answerType() == AnswerType.TEXT) {
            current.setTextAnswer(request.textAnswer());
            current.setAudioFilePath(null);
        } else {
            current.setAudioFilePath(request.audioFilePath());
            current.setTextAnswer(null);
        }
        current.setAnsweredAt(answeredAt);

        // Calculate time taken backend-side
        if (current.getQuestionStartedAt() != null) {
            long seconds = Duration.between(current.getQuestionStartedAt(), answeredAt).toSeconds();
            current.setTimeTakenSeconds((int) seconds);
        } else if (request.timeTakenSeconds() != null) {
            current.setTimeTakenSeconds(request.timeTakenSeconds());
        } else {
            current.setTimeTakenSeconds(0);
        }

        interviewSessionQuestionRepository.save(current);
    }

    @Override
    @Transactional
    public InterviewProgressResponseDto goToNextQuestion() {
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findByUserAndStatus(user, InterviewStatus.IN_PROGRESS)
                .orElseThrow(() -> new InterviewSessionNotFoundException("No active interview session found."));

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session);
        int index = session.getCurrentQuestionIndex();
        if (index < 0 || index >= questions.size()) {
            throw new ResourceNotFoundException("Current question index out of bounds.");
        }

        InterviewSessionQuestion current = questions.get(index);
        if (current.getStatus() != SessionQuestionStatus.ANSWERED) {
            throw new IllegalStateException("Current question must be answered before moving to the next question.");
        }

        if (index + 1 >= questions.size()) {
            throw new IllegalStateException("No more questions in this interview session.");
        }

        // Move to next
        session.setCurrentQuestionIndex(index + 1);
        interviewSessionRepository.save(session);

        // Update the next question to CURRENT and set start time
        InterviewSessionQuestion nextQuestion = questions.get(index + 1);
        nextQuestion.setStatus(SessionQuestionStatus.CURRENT);
        nextQuestion.setQuestionStartedAt(LocalDateTime.now());
        interviewSessionQuestionRepository.save(nextQuestion);

        return getProgress();
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewProgressResponseDto getProgress() {
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findByUserAndStatus(user, InterviewStatus.IN_PROGRESS)
                .orElseThrow(() -> new InterviewSessionNotFoundException("No active interview session found."));

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session);
        int answeredCount = 0;
        List<InterviewQuestionResponseDto> questionDtos = new ArrayList<>();

        for (InterviewSessionQuestion q : questions) {
            if (q.getStatus() == SessionQuestionStatus.ANSWERED) {
                answeredCount++;
            }
            questionDtos.add(new InterviewQuestionResponseDto(
                    q.getId(),
                    q.getQuestionText(),
                    q.getTopic(),
                    q.getDifficulty(),
                    q.getCategory(),
                    q.getDisplayOrder(),
                    q.getQuestionSource(),
                    q.getStatus()
            ));
        }

        int remaining = session.getTotalQuestions() - answeredCount;

        return new InterviewProgressResponseDto(
                session.getId(),
                session.getMode(),
                session.getStatus(),
                session.getTotalQuestions(),
                session.getCurrentQuestionIndex(),
                answeredCount,
                remaining,
                questionDtos
        );
    }

    @Override
    @Transactional
    public InterviewSessionResponseDto completeInterview() {
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findByUserAndStatus(user, InterviewStatus.IN_PROGRESS)
                .orElseThrow(() -> new InterviewSessionNotFoundException("No active interview session found."));

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session);
        long answeredCount = questions.stream()
                .filter(q -> q.getStatus() == SessionQuestionStatus.ANSWERED)
                .count();

        if (answeredCount < session.getTotalQuestions()) {
            throw new IllegalStateException("Cannot complete interview. All questions must be answered first (answered: " + answeredCount + ", total: " + session.getTotalQuestions() + ").");
        }

        LocalDateTime now = LocalDateTime.now();
        session.setStatus(InterviewStatus.COMPLETED);
        session.setCompletedAt(now);

        if (session.getMode() == InterviewMode.MOCK) {
            session.setCooldownUntil(now.plusHours(cooldownHours));
        }

        InterviewSession savedSession = interviewSessionRepository.save(session);
        return interviewMapper.toDto(savedSession);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private <T> Comparator<QuestionCandidate<T>> getCandidateComparator() {
        return (c1, c2) -> {
            // 1. Never seen first
            if (c1.isNeverSeen() != c2.isNeverSeen()) {
                return c1.isNeverSeen() ? -1 : 1;
            }
            // 2. Least recently seen (oldest lastPracticedAt first)
            if (!c1.isNeverSeen()) {
                int dateCompare = c1.getLastPracticedAt().compareTo(c2.getLastPracticedAt());
                if (dateCompare != 0) {
                    return dateCompare;
                }
            }
            // 3. Least times shown
            if (c1.getTimesShown() != c2.getTimesShown()) {
                return Integer.compare(c1.getTimesShown(), c2.getTimesShown());
            }
            // 4. Random tie-break
            return Double.compare(c1.getRandomValue(), c2.getRandomValue());
        };
    }

    private static class QuestionCandidate<T> {
        private final T question;
        private final boolean neverSeen;
        private final LocalDateTime lastPracticedAt;
        private final int timesShown;
        private final double randomValue;

        public QuestionCandidate(T question, InterviewQuestionExposure exposure) {
            this.question = question;
            this.randomValue = Math.random();
            if (exposure == null) {
                this.neverSeen = true;
                this.lastPracticedAt = LocalDateTime.MIN;
                this.timesShown = 0;
            } else {
                this.neverSeen = false;
                this.lastPracticedAt = exposure.getLastPracticedAt();
                this.timesShown = exposure.getTimesShown() != null ? exposure.getTimesShown() : 0;
            }
        }

        public T getQuestion() { return question; }
        public boolean isNeverSeen() { return neverSeen; }
        public LocalDateTime getLastPracticedAt() { return lastPracticedAt; }
        public int getTimesShown() { return timesShown; }
        public double getRandomValue() { return randomValue; }
    }
}
