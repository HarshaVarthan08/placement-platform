import React from 'react';
import { Box, Typography, Link, useTheme } from '@mui/material';
import { footerData } from './footerData';

export const FooterLinks: React.FC = () => {
  const theme = useTheme();

  return (
    <>
      {footerData.sections.map((section) => (
        <Box
          key={section.title}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: 2.5,
          }}
        >
          {/* Section Heading */}
          <Typography
            variant="subtitle2"
            component="h3"
            sx={{
              color: 'common.white',
              fontWeight: 700,
              fontSize: '0.875rem',
              letterSpacing: '0.05em',
              textTransform: 'uppercase',
            }}
          >
            {section.title}
          </Typography>

          {/* Links List */}
          <Box
            component="ul"
            sx={{
              listStyle: 'none',
              p: 0,
              m: 0,
              display: 'flex',
              flexDirection: 'column',
              gap: 1.5,
            }}
          >
            {section.links.map((link) => {
              const isComingSoon = link.isComingSoon;
              const isPlaceholder = link.isPlaceholder;

              return (
                <Box component="li" key={link.label} sx={{ display: 'flex', alignItems: 'center' }}>
                  {isComingSoon ? (
                    <Typography
                      variant="body2"
                      sx={{
                        color: 'rgba(255, 255, 255, 0.4)',
                        fontSize: '0.9rem',
                        display: 'inline-flex',
                        alignItems: 'center',
                        gap: 1,
                        cursor: 'default',
                        userSelect: 'none',
                      }}
                    >
                      {link.label}
                      <Box
                        component="span"
                        sx={{
                          fontSize: '0.7rem',
                          fontWeight: 600,
                          backgroundColor: 'rgba(255, 255, 255, 0.08)',
                          color: 'rgba(255, 255, 255, 0.5)',
                          px: 1,
                          py: 0.25,
                          borderRadius: '4px',
                          textTransform: 'uppercase',
                        }}
                      >
                        Soon
                      </Box>
                    </Typography>
                  ) : (
                    <Link
                      href={link.href}
                      variant="body2"
                      underline="none"
                      sx={{
                        color: isPlaceholder
                          ? 'rgba(255, 255, 255, 0.55)'
                          : 'rgba(255, 255, 255, 0.75)',
                        fontSize: '0.9rem',
                        transition: 'color 0.2s ease-in-out',
                        outline: 'none',
                        display: 'inline-block',
                        minHeight: 24, // Touch target height assist
                        '&:hover': {
                          color: 'primary.light',
                        },
                        '&:focus-visible': {
                          color: 'primary.light',
                          outline: `1px solid ${theme.palette.primary.light}`,
                          outlineOffset: '4px',
                          borderRadius: '2px',
                        },
                      }}
                    >
                      {link.label}
                    </Link>
                  )}
                </Box>
              );
            })}
          </Box>
        </Box>
      ))}
    </>
  );
};

export default FooterLinks;
