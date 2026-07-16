import React from 'react';
import { Grid } from '@mui/material';
import { FEATURES_LIST } from '../../../constants/features';
import FeatureModule from './FeatureModule';
import {
  ResumePreview,
  CoachPreview,
  CodingPreview,
  InterviewPreview,
  RoadmapPreview,
  AnalyticsPreview,
} from './visualizations';

export const FeatureGrid: React.FC = () => {
  const renderVisualization = (id: string) => {
    switch (id) {
      case 'resume-analyzer':
        return <ResumePreview />;
      case 'ai-coach':
        return <CoachPreview />;
      case 'coding-practice':
        return <CodingPreview />;
      case 'mock-interview':
        return <InterviewPreview />;
      case 'career-roadmap':
        return <RoadmapPreview />;
      case 'analytics-dashboard':
        return <AnalyticsPreview />;
      default:
        return null;
    }
  };

  return (
    <Grid
      container
      spacing={4}
      sx={{
        width: '100%',
        margin: 0,
        alignItems: 'stretch',
        '& > .MuiGrid-item': {
          paddingLeft: { xs: 0, sm: 4 }, // Prevent layout shifts / alignment issues
          paddingTop: { xs: 4, sm: 4 },
        },
      }}
    >
      {FEATURES_LIST.map((feature) => (
        <Grid
          key={feature.id}
          size={{ xs: 12, sm: 6 }}
          sx={{
            display: 'flex',
          }}
        >
          <FeatureModule
            title={feature.title}
            description={feature.description}
            icon={feature.icon}
            futureRoute={feature.futureRoute}
            disabled={feature.disabled}
            animationDelay={feature.animationDelay}
          >
            {renderVisualization(feature.id)}
          </FeatureModule>
        </Grid>
      ))}
    </Grid>
  );
};

export default FeatureGrid;
