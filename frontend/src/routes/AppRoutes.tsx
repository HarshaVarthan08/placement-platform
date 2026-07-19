import { Routes, Route, Navigate } from 'react-router-dom';
import { PublicLayout } from '../layouts';
import { ROUTES } from '../constants';
import { Landing, Login, Register, ForgotPassword, VerifyEmail, NotFound } from '../pages';
import { ProtectedRoute } from './ProtectedRoute';
import { PublicOnlyRoute } from './PublicOnlyRoute';
import { Box, Typography } from '@mui/material';

// Minimal placeholders for private routes used for route validation
const Placeholder = ({ name }: { name: string }) => (
  <Box
    sx={{
      p: 4,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '80vh',
      backgroundColor: 'background.default',
      color: 'text.primary',
    }}
  >
    <Typography variant="h3" gutterBottom>
      {name} Page
    </Typography>
    <Typography variant="h6" color="text.secondary">
      This is a minimal placeholder for the {name.toLowerCase()} route.
    </Typography>
  </Box>
);

export const AppRoutes = () => {
  return (
    <Routes>
      {/* Public & Guest Routes using PublicLayout */}
      <Route element={<PublicLayout />}>
        <Route path={ROUTES.PUBLIC.LANDING} element={<Landing />} />

        {/* Guest-only routes */}
        <Route
          path={ROUTES.GUEST.LOGIN}
          element={
            <PublicOnlyRoute>
              <Login />
            </PublicOnlyRoute>
          }
        />
        <Route
          path={ROUTES.GUEST.REGISTER}
          element={
            <PublicOnlyRoute>
              <Register />
            </PublicOnlyRoute>
          }
        />
        <Route
          path={ROUTES.GUEST.FORGOT_PASSWORD}
          element={
            <PublicOnlyRoute>
              <ForgotPassword />
            </PublicOnlyRoute>
          }
        />
        <Route
          path={ROUTES.GUEST.VERIFY_EMAIL}
          element={
            <PublicOnlyRoute>
              <VerifyEmail />
            </PublicOnlyRoute>
          }
        />

        <Route path={ROUTES.PUBLIC.NOT_FOUND} element={<NotFound />} />
      </Route>

      {/* Protected Routes */}
      <Route
        path={ROUTES.PRIVATE.DASHBOARD}
        element={
          <ProtectedRoute>
            <Placeholder name="Dashboard" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.RESUME}
        element={
          <ProtectedRoute>
            <Placeholder name="Resume Analyzer" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.INTERVIEW}
        element={
          <ProtectedRoute>
            <Placeholder name="Interview Prep" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.COMPANY}
        element={
          <ProtectedRoute>
            <Placeholder name="Company Prep" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.APPLICATIONS}
        element={
          <ProtectedRoute>
            <Placeholder name="Applications" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.PROGRESS}
        element={
          <ProtectedRoute>
            <Placeholder name="Progress" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.PROFILE}
        element={
          <ProtectedRoute>
            <Placeholder name="Profile" />
          </ProtectedRoute>
        }
      />
      <Route
        path={ROUTES.PRIVATE.SETTINGS}
        element={
          <ProtectedRoute>
            <Placeholder name="Settings" />
          </ProtectedRoute>
        }
      />

      {/* Wildcard Fallback */}
      <Route path="*" element={<Navigate to={ROUTES.PUBLIC.NOT_FOUND} replace />} />
    </Routes>
  );
};

export default AppRoutes;
