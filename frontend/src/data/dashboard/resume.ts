import type { ResumeInsightsData } from '../../types/dashboard';

export const resumeMockData: ResumeInsightsData = {
  overallScore: 82,
  atsScore: 85,
  grammarScore: 90,
  formattingScore: 80,
  keywordScore: 75,
  projectsScore: 88,
  skillsScore: 85,
  educationScore: 95,
  strengths: [
    'Strong profile description highlighting technical experience.',
    'Consistent font choices and professional template alignment.',
    'Quantified bullet points detailing project outcomes.',
  ],
  weaknesses: [
    'Several industry-standard keywords are missing for target roles.',
    'Project descriptions could utilize more action verbs.',
    'Formatting spacing is slightly uneven in the experience section.',
  ],
  missingKeywords: ['System Design', 'Kubernetes', 'CI/CD Pipelines', 'RESTful APIs', 'TypeScript'],
  suggestions: [
    'Incorporate keywords like "System Design" and "CI/CD Pipelines" into your project descriptions.',
    'Start every bullet point in the Experience section with an active verb (e.g., "Led", "Designed", "Optimized").',
    'Ensure uniform padding and spacing of 10px between layout segments.',
  ],
};
