import type { GreetingData } from '../../types/dashboard';

// We export a baseline greeting config. The userName is supplied dynamically.
export const getGreetingMockData = (userName: string): GreetingData => {
  const hours = new Date().getHours();
  let greetingWord = 'Good Morning';
  if (hours >= 12 && hours < 17) {
    greetingWord = 'Good Afternoon';
  } else if (hours >= 17) {
    greetingWord = 'Good Evening';
  }

  // Get readable current date
  const currentDate = new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return {
    greeting: greetingWord,
    currentDate,
    userName,
    motivationalMessage: "Keep preparing. You're making steady progress toward your dream job!",
    focusArea: 'Continue practicing interview simulation and DSA sorting algorithms today.',
    currentGoal: 'Target Role: Full-Stack Engineer at Tier 1 Tech',
    profileCompletion: 90,
    placementReadiness: 82,
    currentStreak: 12,
  };
};
export default getGreetingMockData;
