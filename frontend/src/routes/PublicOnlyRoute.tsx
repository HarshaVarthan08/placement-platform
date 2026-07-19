import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks';
import { ROUTES } from '../constants';
import { Box, CircularProgress } from '@mui/material';

interface PublicOnlyRouteProps {
  children: React.ReactNode;
}

export const PublicOnlyRoute: React.FC<PublicOnlyRouteProps> = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
        }}
      >
        <CircularProgress size={40} />
      </Box>
    );
  }

  if (isAuthenticated) {
    return <Navigate to={ROUTES.PRIVATE.DASHBOARD} replace />;
  }

  return <>{children}</>;
};

export default PublicOnlyRoute;
