import type { MouseEvent } from 'react';
import { Link, useTheme } from '@mui/material';
import { Link as RouterLink, useLocation, useNavigate } from 'react-router-dom';

export interface NavigationItemProps {
  label: string;
  to: string;
  isHash?: boolean;
  onClick?: () => void;
}

export const NavigationItem = ({ label, to, isHash, onClick }: NavigationItemProps) => {
  const location = useLocation();
  const navigate = useNavigate();
  const theme = useTheme();

  // Determine if active using location.pathname and location.hash
  const isActive = isHash
    ? location.pathname === '/' && location.hash === to.substring(to.indexOf('#'))
    : location.pathname === to;

  // Handler for hash links to fail gracefully or scroll if element exists
  const handleClick = (e: MouseEvent<HTMLAnchorElement>) => {
    if (onClick) {
      onClick();
    }

    if (isHash) {
      const targetId = to.substring(to.indexOf('#') + 1);
      const element = document.getElementById(targetId);
      if (element) {
        e.preventDefault();
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      } else {
        // If element doesn't exist, we let it fall back gracefully.
        // If we are not on the landing page, we want standard navigation to '/' with hash.
        if (location.pathname === '/') {
          // On landing page, but element doesn't exist yet, we prevent default jump
          // to avoid awkward layout hops, but navigate programmatically via react-router
          // so it triggers a state update and highlights correctly.
          e.preventDefault();
          navigate(to);
        }
      }
    }
  };

  return (
    <Link
      component={RouterLink}
      to={to}
      onClick={handleClick}
      sx={{
        color: isActive ? 'primary.main' : 'text.secondary',
        fontSize: '0.875rem', // body2 size (14px)
        fontWeight: isActive ? 'fontWeightSemiBold' : 'fontWeightMedium', // 600 or 500
        textDecoration: 'none',
        transition: theme.transitions.create(['color'], {
          easing: theme.transitions.easing.easeInOut,
          duration: theme.transitions.duration.shorter,
        }),
        '&:hover': {
          color: 'primary.main',
        },
        '&:focus-visible': {
          outline: '2px solid',
          outlineColor: 'primary.main',
          outlineOffset: '4px',
          borderRadius: '4px',
        },
        padding: '6px 12px',
        display: 'inline-flex',
        alignItems: 'center',
      }}
      aria-current={isActive ? 'page' : undefined}
    >
      {label}
    </Link>
  );
};

export default NavigationItem;
