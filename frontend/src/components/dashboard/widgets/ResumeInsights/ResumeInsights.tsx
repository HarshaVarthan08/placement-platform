import React, { useState } from 'react';
import { Box, Typography, Grid, LinearProgress, Tab, Tabs, Chip } from '@mui/material';
import { CheckCircle2, AlertCircle, FileSpreadsheet, Lightbulb } from 'lucide-react';
import type { ResumeInsightsData } from '../../../../types/dashboard';
import { WidgetCard } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface ResumeInsightsProps {
  data?: ResumeInsightsData;
  loading?: boolean;
  error?: Error | null;
}

export const ResumeInsights: React.FC<ResumeInsightsProps> = ({
  data,
  loading = false,
  error = null,
}) => {
  const [activeTab, setActiveTab] = useState<number>(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  const getMetricColor = (val: number) => {
    if (val >= 85) return 'success';
    if (val >= 70) return 'warning';
    return 'error';
  };

  return (
    <WidgetCard loading={loading} loadingVariant="card" error={error} isEmpty={!data}>
      {data && (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
          <Box sx={{ mb: 2 }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              {DASHBOARD_CONSTANTS.SECTIONS.RESUME_INSIGHTS.TITLE}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {DASHBOARD_CONSTANTS.SECTIONS.RESUME_INSIGHTS.DESCRIPTION}
            </Typography>
          </Box>

          <Grid container spacing={3} sx={{ mb: 2 }}>
            {/* Radial score gauge */}
            <Grid
              size={{ xs: 12, sm: 4 }}
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <Box
                sx={{
                  position: 'relative',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  width: 110,
                  height: 110,
                  borderRadius: '50%',
                  background: (theme) =>
                    `conic-gradient(${theme.palette.success.main} ${data.overallScore * 3.6}deg, ${theme.palette.action.hover} 0deg)`,
                  boxShadow: 'inset 0 0 0 10px white',
                  mb: 1.5,
                }}
              >
                {/* Center text cut-out */}
                <Box
                  sx={{
                    position: 'absolute',
                    width: 90,
                    height: 90,
                    borderRadius: '50%',
                    backgroundColor: 'background.paper',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  <Typography
                    variant="h4"
                    sx={{ fontWeight: 800, color: 'success.dark', lineHeight: 1 }}
                  >
                    {data.overallScore}
                  </Typography>
                  <Typography
                    variant="caption"
                    color="text.secondary"
                    sx={{ fontSize: '0.6875rem', fontWeight: 600 }}
                  >
                    SCORE / 100
                  </Typography>
                </Box>
              </Box>
              <Typography variant="subtitle2" sx={{ fontWeight: 700, color: 'success.dark' }}>
                Excellent Standing
              </Typography>
            </Grid>

            {/* Sub-metric category bars */}
            <Grid size={{ xs: 12, sm: 8 }}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.25 }}>
                {/* ATS score */}
                <Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.25 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                      ATS Keyword Match
                    </Typography>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                      {data.atsScore}%
                    </Typography>
                  </Box>
                  <LinearProgress
                    variant="determinate"
                    value={data.atsScore}
                    color={getMetricColor(data.atsScore)}
                    sx={{ height: 6, borderRadius: 3 }}
                  />
                </Box>

                {/* Grammar Score */}
                <Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.25 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                      Grammar & Syntax
                    </Typography>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                      {data.grammarScore}%
                    </Typography>
                  </Box>
                  <LinearProgress
                    variant="determinate"
                    value={data.grammarScore}
                    color={getMetricColor(data.grammarScore)}
                    sx={{ height: 6, borderRadius: 3 }}
                  />
                </Box>

                {/* Formatting Score */}
                <Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.25 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                      Formatting & Spacing
                    </Typography>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                      {data.formattingScore}%
                    </Typography>
                  </Box>
                  <LinearProgress
                    variant="determinate"
                    value={data.formattingScore}
                    color={getMetricColor(data.formattingScore)}
                    sx={{ height: 6, borderRadius: 3 }}
                  />
                </Box>

                {/* Technical Skills Score */}
                <Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.25 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                      Project Details Depth
                    </Typography>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                      {data.projectsScore}%
                    </Typography>
                  </Box>
                  <LinearProgress
                    variant="determinate"
                    value={data.projectsScore}
                    color={getMetricColor(data.projectsScore)}
                    sx={{ height: 6, borderRadius: 3 }}
                  />
                </Box>
              </Box>
            </Grid>
          </Grid>

          {/* Details tab selector */}
          <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 2 }}>
            <Tabs
              value={activeTab}
              onChange={handleTabChange}
              aria-label="resume details options"
              variant="scrollable"
              scrollButtons="auto"
              sx={{ minHeight: 36 }}
            >
              <Tab label="Strengths" sx={{ fontSize: '0.75rem', py: 1, minHeight: 36 }} />
              <Tab label="Weaknesses" sx={{ fontSize: '0.75rem', py: 1, minHeight: 36 }} />
              <Tab label="Missing Keywords" sx={{ fontSize: '0.75rem', py: 1, minHeight: 36 }} />
              <Tab label="Suggestions" sx={{ fontSize: '0.75rem', py: 1, minHeight: 36 }} />
            </Tabs>
          </Box>

          {/* Tab content panels */}
          <Box sx={{ flexGrow: 1, overflowY: 'auto', maxHeight: 180 }}>
            {activeTab === 0 && (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                {data.strengths.map((str, idx) => (
                  <Box key={idx} sx={{ display: 'flex', alignItems: 'flex-start', gap: 1 }}>
                    <Box sx={{ color: 'success.main', display: 'flex', mt: 0.5, flexShrink: 0 }}>
                      <CheckCircle2 size={16} />
                    </Box>
                    <Typography variant="body2" color="text.primary">
                      {str}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}

            {activeTab === 1 && (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                {data.weaknesses.map((weak, idx) => (
                  <Box key={idx} sx={{ display: 'flex', alignItems: 'flex-start', gap: 1 }}>
                    <Box sx={{ color: 'error.main', display: 'flex', mt: 0.5, flexShrink: 0 }}>
                      <AlertCircle size={16} />
                    </Box>
                    <Typography variant="body2" color="text.primary">
                      {weak}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}

            {activeTab === 2 && (
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, p: 0.5 }}>
                {data.missingKeywords.map((keyword, idx) => (
                  <Chip
                    key={idx}
                    label={keyword}
                    color="error"
                    variant="outlined"
                    size="small"
                    icon={<FileSpreadsheet size={12} />}
                    sx={{ fontWeight: 600, fontSize: '0.75rem' }}
                  />
                ))}
              </Box>
            )}

            {activeTab === 3 && (
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                {data.suggestions.map((sug, idx) => (
                  <Box key={idx} sx={{ display: 'flex', alignItems: 'flex-start', gap: 1 }}>
                    <Box sx={{ color: 'warning.main', display: 'flex', mt: 0.5, flexShrink: 0 }}>
                      <Lightbulb size={16} />
                    </Box>
                    <Typography variant="body2" color="text.primary">
                      {sug}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}
          </Box>
        </Box>
      )}
    </WidgetCard>
  );
};

export default ResumeInsights;
