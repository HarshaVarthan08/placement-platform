import React from 'react';
import { Box, IconButton, Tooltip, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { footerData } from './footerData';

export const FooterSocial: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        gap: 1.5,
      }}
    >
      {footerData.socialLinks.map((social) => {
        const Icon = social.icon;
        return (
          <Tooltip key={social.name} title={social.name} arrow>
            <IconButton
              component="a"
              href={social.href}
              target="_blank"
              rel="noopener noreferrer"
              aria-label={social.ariaLabel}
              sx={{
                width: 44,
                height: 44,
                borderRadius: '50%',
                backgroundColor: 'rgba(255, 255, 255, 0.03)',
                color: 'rgba(255, 255, 255, 0.7)',
                border: '1px solid rgba(255, 255, 255, 0.08)',
                transition: 'all 0.25s ease-in-out',
                outline: 'none',
                '&:hover': {
                  backgroundColor: alpha(theme.palette.primary.main, 0.15),
                  borderColor: theme.palette.primary.light,
                  color: theme.palette.primary.light,
                  transform: 'translateY(-2px)',
                },
                '&:focus-visible': {
                  borderColor: theme.palette.primary.light,
                  boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.light, 0.35)}`,
                  backgroundColor: 'rgba(255, 255, 255, 0.08)',
                },
              }}
            >
              {social.name === 'Email' ? <Icon size={20} /> : <Icon sx={{ fontSize: 20 }} />}
            </IconButton>
          </Tooltip>
        );
      })}
    </Box>
  );
};

export default FooterSocial;
