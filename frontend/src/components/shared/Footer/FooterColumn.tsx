import type { MouseEvent } from 'react';
import { Box, Typography, Link, useTheme } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import type { FooterLink } from '../../../constants';

export interface FooterColumnProps {
  title: string;
  links: FooterLink[];
}

export const FooterColumn = ({ title, links }: FooterColumnProps) => {
  const theme = useTheme();
  const location = useLocation();

  const handleLinkClick = (e: MouseEvent<HTMLAnchorElement>, to: string) => {
    if (to.startsWith('#') || to.includes('#')) {
      const hashIndex = to.indexOf('#');
      const targetId = to.substring(hashIndex + 1);
      const element = document.getElementById(targetId);
      if (element) {
        e.preventDefault();
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      } else {
        const path = to.substring(0, hashIndex);
        if (location.pathname === '/' || path === '') {
          // On landing page, but target doesn't exist, fail gracefully without runtime errors
          e.preventDefault();
          window.history.pushState(null, '', to);
        }
      }
    }
  };

  return (
    <Box>
      <Typography
        variant="h4"
        component="h3"
        sx={{
          fontWeight: 'fontWeightSemiBold',
          color: 'text.primary',
          marginBottom: 2,
          fontSize: '0.875rem', // body2 size
          letterSpacing: '0.05em',
          textTransform: 'uppercase',
        }}
      >
        {title}
      </Typography>
      <Box
        component="ul"
        sx={{
          listStyle: 'none',
          padding: 0,
          margin: 0,
          display: 'flex',
          flexDirection: 'column',
          gap: 1.5,
        }}
      >
        {links.map((link) => (
          <Box component="li" key={link.label}>
            <Link
              component={RouterLink}
              to={link.to}
              onClick={(e) => handleLinkClick(e, link.to)}
              sx={{
                color: 'text.secondary',
                fontSize: '0.875rem', // body2
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
              }}
            >
              {link.label}
            </Link>
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default FooterColumn;
