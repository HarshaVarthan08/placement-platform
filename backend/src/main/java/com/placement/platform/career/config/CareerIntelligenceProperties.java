package com.placement.platform.career.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "career")
public class CareerIntelligenceProperties {

    private final Confidence confidence = new Confidence();
    private final Preparation preparation = new Preparation();
    private final Difficulty difficulty = new Difficulty();

    public Confidence getConfidence() {
        return confidence;
    }

    public Preparation getPreparation() {
        return preparation;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public static class Confidence {
        private int matchWeight = 40;
        private int placementWeight = 25;
        private int resumeWeight = 20;
        private int interviewWeight = 15;

        public int getMatchWeight() {
            return matchWeight;
        }

        public void setMatchWeight(int matchWeight) {
            this.matchWeight = matchWeight;
        }

        public int getPlacementWeight() {
            return placementWeight;
        }

        public void setPlacementWeight(int placementWeight) {
            this.placementWeight = placementWeight;
        }

        public int getResumeWeight() {
            return resumeWeight;
        }

        public void setResumeWeight(int resumeWeight) {
            this.resumeWeight = resumeWeight;
        }

        public int getInterviewWeight() {
            return interviewWeight;
        }

        public void setInterviewWeight(int interviewWeight) {
            this.interviewWeight = interviewWeight;
        }
    }

    public static class Preparation {
        private Weeks weeks = new Weeks();

        public Weeks getWeeks() {
            return weeks;
        }

        public void setWeeks(Weeks weeks) {
            this.weeks = weeks;
        }

        public static class Weeks {
            private int low = 2;
            private int medium = 4;
            private int high = 6;
            private int veryHigh = 8;

            public int getLow() {
                return low;
            }

            public void setLow(int low) {
                this.low = low;
            }

            public int getMedium() {
                return medium;
            }

            public void setMedium(int medium) {
                this.medium = medium;
            }

            public int getHigh() {
                return high;
            }

            public void setHigh(int high) {
                this.high = high;
            }

            public int getVeryHigh() {
                return veryHigh;
            }

            public void setVeryHigh(int veryHigh) {
                this.veryHigh = veryHigh;
            }
        }
    }

    public static class Difficulty {
        private int veryHighThreshold = 10;
        private int highThreshold = 7;
        private int mediumThreshold = 4;

        public int getVeryHighThreshold() {
            return veryHighThreshold;
        }

        public void setVeryHighThreshold(int veryHighThreshold) {
            this.veryHighThreshold = veryHighThreshold;
        }

        public int getHighThreshold() {
            return highThreshold;
        }

        public void setHighThreshold(int highThreshold) {
            this.highThreshold = highThreshold;
        }

        public int getMediumThreshold() {
            return mediumThreshold;
        }

        public void setMediumThreshold(int mediumThreshold) {
            this.mediumThreshold = mediumThreshold;
        }
    }
}
