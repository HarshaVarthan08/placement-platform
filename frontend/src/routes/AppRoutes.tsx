import { Routes, Route, Navigate } from 'react-router-dom';
import { PublicLayout, DashboardLayout } from '../layouts';
import { ROUTES } from '../constants';
import {
  Landing,
  Login,
  Register,
  ForgotPassword,
  VerifyEmail,
  NotFound,
  FeaturePlaceholder,
  DashboardHome,
} from '../pages';
import { ProtectedRoute } from './ProtectedRoute';
import { PublicOnlyRoute } from './PublicOnlyRoute';

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

      {/* Protected Routes nested within DashboardLayout */}
      <Route
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route path={ROUTES.PRIVATE.DASHBOARD} element={<DashboardHome />} />
        <Route
          path={ROUTES.PRIVATE.RESUME}
          element={
            <FeaturePlaceholder
              pageTitle="Resume Analyzer"
              description="Analyze your resume against job descriptions, optimize keywords, and boost your ATS score."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.INTERVIEW}
          element={
            <FeaturePlaceholder
              pageTitle="Interview Prep"
              description="Practice with simulated mock interviews, receive real-time feedback, and master technical questions."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.COMPANY}
          element={
            <FeaturePlaceholder
              pageTitle="Company Prep"
              description="Research company profiles, track specific targets, and prepare custom question guides."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.APPLICATIONS}
          element={
            <FeaturePlaceholder
              pageTitle="Applications"
              description="Track job applications, manage stages, and monitor interview schedules."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.PROGRESS}
          element={
            <FeaturePlaceholder
              pageTitle="Progress"
              description="Monitor prep completion, track metrics history, and hit your milestones."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.PROFILE}
          element={
            <FeaturePlaceholder
              pageTitle="Profile"
              description="Manage your personal details, academic credentials, and career preferences."
            />
          }
        />
        <Route
          path={ROUTES.PRIVATE.SETTINGS}
          element={
            <FeaturePlaceholder
              pageTitle="Settings"
              description="Configure account preferences, notifications toggle, and security properties."
            />
          }
        />
      </Route>

      {/* Wildcard Fallback */}
      <Route path="*" element={<Navigate to={ROUTES.PUBLIC.NOT_FOUND} replace />} />
    </Routes>
  );
};

export default AppRoutes;
