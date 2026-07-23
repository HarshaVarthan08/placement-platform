import React from 'react';
import { Box, Skeleton } from '@mui/material';

interface LoadingSkeletonProps {
  variant?: 'metric' | 'list' | 'progress' | 'banner' | 'card';
  rows?: number;
}

export const LoadingSkeleton: React.FC<LoadingSkeletonProps> = ({ variant = 'card', rows = 3 }) => {
  if (variant === 'metric') {
    return (
      <Box sx={{ py: 1 }}>
        <Skeleton variant="text" width="60%" height={24} sx={{ mb: 1 }} />
        <Skeleton variant="text" width="40%" height={48} sx={{ mb: 1 }} />
        <Skeleton variant="text" width="80%" height={20} />
      </Box>
    );
  }

  if (variant === 'list') {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, width: '100%', py: 1 }}>
        {Array.from({ length: rows }).map((_, idx) => (
          <Box key={idx} sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Skeleton variant="circular" width={40} height={40} />
            <Box sx={{ flexGrow: 1 }}>
              <Skeleton variant="text" width="80%" height={20} />
              <Skeleton variant="text" width="50%" height={16} />
            </Box>
          </Box>
        ))}
      </Box>
    );
  }

  if (variant === 'progress') {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, width: '100%', py: 1 }}>
        {Array.from({ length: rows }).map((_, idx) => (
          <Box key={idx} sx={{ width: '100%' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
              <Skeleton variant="text" width="40%" height={20} />
              <Skeleton variant="text" width="15%" height={20} />
            </Box>
            <Skeleton variant="rectangular" width="100%" height={8} sx={{ borderRadius: 1 }} />
          </Box>
        ))}
      </Box>
    );
  }

  if (variant === 'banner') {
    return (
      <Box sx={{ p: 1, width: '100%' }}>
        <Skeleton variant="text" width="30%" height={32} sx={{ mb: 1 }} />
        <Skeleton variant="text" width="60%" height={20} sx={{ mb: 2 }} />
        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 3, mt: 2 }}>
          <Box sx={{ flex: 1 }}>
            <Skeleton variant="text" width="40%" height={16} sx={{ mb: 1 }} />
            <Skeleton variant="rectangular" width="100%" height={12} sx={{ borderRadius: 1 }} />
          </Box>
          <Box sx={{ flex: 1 }}>
            <Skeleton variant="text" width="40%" height={16} sx={{ mb: 1 }} />
            <Skeleton variant="rectangular" width="100%" height={12} sx={{ borderRadius: 1 }} />
          </Box>
        </Box>
      </Box>
    );
  }

  return (
    <Box sx={{ width: '100%', py: 1 }}>
      <Skeleton variant="rectangular" width="100%" height={120} sx={{ borderRadius: 2 }} />
    </Box>
  );
};

export default LoadingSkeleton;
