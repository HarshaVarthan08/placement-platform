import React from 'react';
import { Breadcrumbs as MUIBreadcrumbs, Link, Typography } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import { ChevronRight } from 'lucide-react';

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

interface BreadcrumbsProps {
  separator?: React.ReactNode;
}

export const Breadcrumbs: React.FC<BreadcrumbsProps> = ({
  separator = <ChevronRight size={14} />,
}) => {
  const location = useLocation();
  const currentPath = location.pathname;

  // Generate dynamic breadcrumbs based on active route
  const getCrumbs = () => {
    const crumbs = [{ label: 'Home', path: '/' }];

    if (currentPath === '/dashboard') {
      crumbs.push({ label: 'Dashboard', path: '/dashboard' });
    } else {
      crumbs.push({ label: 'Dashboard', path: '/dashboard' });
      const currentTitle = routeTitleMap[currentPath] || 'Feature';
      crumbs.push({ label: currentTitle, path: currentPath });
    }

    return crumbs;
  };

  const crumbs = getCrumbs();

  return (
    <MUIBreadcrumbs
      separator={separator}
      aria-label="breadcrumb"
      sx={{
        color: 'text.secondary',
        '& .MuiBreadcrumbs-separator': {
          mx: 1,
          display: 'flex',
          alignItems: 'center',
        },
      }}
    >
      {crumbs.map((crumb, index) => {
        const isLast = index === crumbs.length - 1;

        if (isLast) {
          return (
            <Typography
              key={crumb.path}
              variant="body2"
              sx={{
                fontWeight: 600,
                color: 'text.primary',
                fontSize: '0.875rem',
              }}
            >
              {crumb.label}
            </Typography>
          );
        }

        return (
          <Link
            key={crumb.path}
            underline="hover"
            component={RouterLink}
            to={crumb.path}
            sx={{
              fontSize: '0.875rem',
              color: 'text.secondary',
              fontWeight: 500,
              '&:hover': {
                color: 'primary.main',
              },
              transition: 'color 0.2s ease',
            }}
          >
            {crumb.label}
          </Link>
        );
      })}
    </MUIBreadcrumbs>
  );
};

export default Breadcrumbs;
