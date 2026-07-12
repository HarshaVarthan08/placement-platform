package com.placement.platform.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppLoggers {
    public static final Logger AUTH = LoggerFactory.getLogger("Authentication");
    public static final Logger RESUME = LoggerFactory.getLogger("Resume");
    public static final Logger INTERVIEW = LoggerFactory.getLogger("Interview");
    public static final Logger PLACEMENT = LoggerFactory.getLogger("Placement");
    public static final Logger JOB_SYNC = LoggerFactory.getLogger("JobSynchronization");
    public static final Logger RECOMMENDATIONS = LoggerFactory.getLogger("Recommendations");
    public static final Logger CAREER = LoggerFactory.getLogger("CareerIntelligence");
    public static final Logger PREMIUM = LoggerFactory.getLogger("Premium");
    public static final Logger SUBSCRIPTION = LoggerFactory.getLogger("Subscription");
    public static final Logger ADMIN = LoggerFactory.getLogger("Administration");
    public static final Logger REQUEST = LoggerFactory.getLogger("RequestLogging");

    private AppLoggers() {}
}
