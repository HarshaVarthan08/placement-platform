import type { PlacementJourneyStage } from '../../types/dashboard';

export const placementMockData: PlacementJourneyStage[] = [
  {
    id: 'stage-1',
    name: 'Resume Building & Optimization',
    status: 'COMPLETED',
    progress: 100,
    current: false,
  },
  {
    id: 'stage-2',
    name: 'Technical Skill Assessment',
    status: 'COMPLETED',
    progress: 100,
    current: false,
  },
  {
    id: 'stage-3',
    name: 'Job Applications Tracking',
    status: 'IN_PROGRESS',
    progress: 60,
    current: true,
  },
  {
    id: 'stage-4',
    name: 'Technical & HR Interviews',
    status: 'NOT_STARTED',
    progress: 0,
    current: false,
  },
  {
    id: 'stage-5',
    name: 'Placement Offers & Review',
    status: 'NOT_STARTED',
    progress: 0,
    current: false,
  },
];
