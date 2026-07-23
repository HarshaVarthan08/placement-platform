import React from 'react';
import { useLocation } from 'react-router-dom';
import {
  Box,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  InputBase,
  Badge,
  useTheme,
  useMediaQuery,
  Tooltip,
} from '@mui/material';
import { Menu, Search, Bell, Sun } from 'lucide-react';
import { useUI } from '../../../hooks/useUI';
import Breadcrumbs from './Breadcrumbs';
import UserDropdown from './UserDropdown';

const routeTitleMap: Record<string, string> = {
  '/dashboard': 'Dashboard',
  '/resume-analyzer': 'Resume Analyzer',
  '/interview-prep': 'Interview Prep',
  '/company-prep': 'Company Prep',
  '/applications': 'Applications',
  '/progress': 'Progress',
  '/profile': 'Profile',
  '/settings': 'Settings',
};

export const Topbar: React.FC = () => {
  const location = useLocation();
  const { toggleMobileSidebar } = useUI();

  const theme = useTheme();
  // Match MUI 'md' breakpoint (1024px)
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const currentPath = location.pathname;
  const pageTitle = routeTitleMap[currentPath] || 'AI Placement Platform';

  return (
    <AppBar
      position="sticky"
      color="default"
      elevation={0}
      sx={{
        backgroundColor: 'background.paper',
        borderBottom: '1px solid',
        borderColor: 'divider',
        zIndex: 1100,
        height: 64,
        justifyContent: 'center',
      }}
      aria-label="Dashboard Topbar"
    >
      <Toolbar
        sx={{
          justifyContent: 'space-between',
          px: { xs: 2, md: 3 },
          minHeight: '64px !important',
        }}
      >
        {/* Left Side: Mobile Hamburger & Title OR Desktop Breadcrumbs */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
          {!isDesktop && (
            <IconButton
              color="inherit"
              aria-label="Open menu drawer"
              edge="start"
              onClick={toggleMobileSidebar}
              sx={{ color: 'text.primary' }}
            >
              <Menu size={22} />
            </IconButton>
          )}

          {isDesktop ? (
            <Breadcrumbs />
          ) : (
            <Typography
              variant="subtitle1"
              sx={{
                fontWeight: 700,
                color: 'text.primary',
                fontSize: '1.05rem',
              }}
            >
              {pageTitle}
            </Typography>
          )}
        </Box>

        {/* Right Side: Search, Notifications, Theme, User Avatar */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: { xs: 1, sm: 2 } }}>
          {/* Search Box - Hidden on mobile screen sizes (< 768px) */}
          <Box
            sx={{
              display: { xs: 'none', sm: 'flex' },
              alignItems: 'center',
              backgroundColor: 'neutral.gray50',
              border: '1px solid',
              borderColor: 'border.default',
              borderRadius: 2,
              px: 1.5,
              py: 0.5,
              width: { sm: 200, md: 240 },
              '&:focus-within': {
                borderColor: 'primary.main',
                backgroundColor: 'background.paper',
                boxShadow: '0 0 0 2px rgba(0, 102, 255, 0.15)',
              },
              transition: 'all 0.2s ease',
            }}
          >
            <Search size={16} style={{ color: '#9CA3AF', marginRight: 8 }} />
            <InputBase
              placeholder="Search prep, jobs..."
              inputProps={{ 'aria-label': 'search dashboard content' }}
              sx={{
                fontSize: '0.85rem',
                width: '100%',
                color: 'text.primary',
              }}
            />
          </Box>

          {/* Theme Toggle Placeholder */}
          <Tooltip title="Light/Dark Mode (UI Placeholder)" arrow>
            <IconButton
              color="inherit"
              aria-label="toggle system theme"
              sx={{
                color: 'text.secondary',
                '&:hover': { color: 'text.primary' },
              }}
            >
              <Sun size={20} />
            </IconButton>
          </Tooltip>

          {/* Notification Button Placeholder */}
          <Tooltip title="Notifications (UI Placeholder)" arrow>
            <IconButton
              color="inherit"
              aria-label="view system notifications"
              sx={{
                color: 'text.secondary',
                '&:hover': { color: 'text.primary' },
              }}
            >
              <Badge
                variant="dot"
                color="primary"
                overlap="circular"
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
              >
                <Bell size={20} />
              </Badge>
            </IconButton>
          </Tooltip>

          {/* User Account Menu Dropdown */}
          <UserDropdown />
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Topbar;
