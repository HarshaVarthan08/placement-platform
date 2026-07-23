import { useState, useEffect } from 'react';
import { useAuth } from './useAuth';
import { dashboardService } from '../services/dashboard/dashboardService';
import type { DashboardPayload } from '../types/dashboard';

/**
 * useDashboard Hook
 *
 * Serves as the data-orchestration layer for the Dashboard Home.
 * It resolves the current user session details from AuthContext, delegates queries
 * to the dashboardService, and returns a single state container detailing loading,
 * error, and categorized dashboard properties.
 *
 * This design enables future additions like react-query, SWR, caching, polling,
 * or live API fetch adapters to be swapped in without modifying any layout views.
 */
export const useDashboard = () => {
  const { currentUser } = useAuth();
  const userName = currentUser?.name || 'Student';

  const [state, setState] = useState<{
    loading: boolean;
    error: Error | null;
    data: DashboardPayload | null;
  }>({
    loading: true,
    error: null,
    data: null,
  });

  useEffect(() => {
    let isMounted = true;
    const fetchDashboardData = async () => {
      try {
        setState((prev) => ({ ...prev, loading: true }));
        // Simulate a minor network latency of 400ms to show the loading skeletons
        await new Promise((resolve) => setTimeout(resolve, 400));

        if (!isMounted) return;

        const payload: DashboardPayload = {
          greeting: dashboardService.getDashboardGreeting(userName),
          statistics: dashboardService.getDashboardStatistics(),
          quickActions: dashboardService.getDashboardQuickActions(),
          placementJourney: dashboardService.getDashboardPlacementJourney(),
          resumeInsights: dashboardService.getDashboardResumeInsights(),
          learningProgress: dashboardService.getDashboardLearningProgress(),
          activities: dashboardService.getDashboardActivities(),
          upcomingEvents: dashboardService.getDashboardEvents(),
        };

        setState({
          loading: false,
          error: null,
          data: payload,
        });
      } catch (err) {
        if (!isMounted) return;
        setState({
          loading: false,
          error: err instanceof Error ? err : new Error('Failed to fetch dashboard data'),
          data: null,
        });
      }
    };

    fetchDashboardData();

    return () => {
      isMounted = false;
    };
  }, [userName]);

  return {
    loading: state.loading,
    error: state.error,
    greeting: state.data?.greeting,
    statistics: state.data?.statistics || [],
    quickActions: state.data?.quickActions || [],
    placementJourney: state.data?.placementJourney || [],
    resumeInsights: state.data?.resumeInsights,
    learningProgress: state.data?.learningProgress || [],
    activities: state.data?.activities || [],
    upcomingEvents: state.data?.upcomingEvents || [],
  };
};

export default useDashboard;
