import { Drawer, Box, IconButton, Button, Link, Stack, useTheme } from '@mui/material';
import { X } from 'lucide-react';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import { PUBLIC_NAVIGATION_ITEMS, AUTH_NAVIGATION_ITEMS } from '../../../constants';
import NavbarLogo from './NavbarLogo';
import NavigationItem from './NavigationItem';

export interface MobileDrawerProps {
  open: boolean;
  onClose: () => void;
}

export const MobileDrawer = ({ open, onClose }: MobileDrawerProps) => {
  const theme = useTheme();
  const location = useLocation();
  const isLoginActive = location.pathname === AUTH_NAVIGATION_ITEMS.LOGIN.to;
  const isRegisterActive = location.pathname === AUTH_NAVIGATION_ITEMS.REGISTER.to;

  const handleCloseAndBlur = () => {
    if (document.activeElement instanceof HTMLElement) {
      document.activeElement.blur();
    }
    onClose();
  };

  return (
    <Drawer
      anchor="left"
      open={open}
      onClose={onClose}
      ModalProps={{
        keepMounted: true, // Better open performance on mobile
      }}
      slotProps={{
        paper: {
          sx: {
            width: '100%',
            maxWidth: '320px',
            boxSizing: 'border-box',
            backgroundColor: 'background.paper',
            padding: 3,
            display: 'flex',
            flexDirection: 'column',
          },
        },
      }}
    >
      {/* Drawer Header */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          marginBottom: 4,
        }}
      >
        <NavbarLogo />
        <IconButton
          onClick={handleCloseAndBlur}
          edge="end"
          color="inherit"
          aria-label="Close navigation menu"
          sx={{
            color: 'text.secondary',
            '&:hover': {
              color: 'text.primary',
            },
          }}
        >
          <X size={24} />
        </IconButton>
      </Box>

      {/* Drawer Links */}
      <Stack
        spacing={1}
        component="nav"
        aria-label="Mobile site navigation"
        sx={{
          flexGrow: 1,
          alignItems: 'flex-start',
        }}
      >
        {PUBLIC_NAVIGATION_ITEMS.map((item) => (
          <Box
            key={item.label}
            sx={{
              width: '100%',
              display: 'flex',
            }}
          >
            <NavigationItem
              label={item.label}
              to={item.to}
              isHash={item.isHash}
              onClick={handleCloseAndBlur}
            />
          </Box>
        ))}
      </Stack>

      {/* Auth Actions in Drawer Footer */}
      <Stack spacing={2} sx={{ marginTop: 'auto', paddingTop: 4 }}>
        <Link
          component={RouterLink}
          to={AUTH_NAVIGATION_ITEMS.LOGIN.to}
          onClick={handleCloseAndBlur}
          sx={{
            color: isLoginActive ? 'primary.main' : 'text.primary',
            fontSize: '0.875rem',
            fontWeight: 'fontWeightSemiBold',
            textDecoration: 'none',
            textAlign: 'center',
            '&:hover': {
              color: 'primary.main',
            },
            padding: '10px 16px',
            border: '1px solid',
            borderColor: isLoginActive ? 'primary.main' : 'divider',
            borderRadius: `${theme.customSpacing.borderRadius.small}px`,
            display: 'block',
          }}
        >
          {AUTH_NAVIGATION_ITEMS.LOGIN.label}
        </Link>
        <Button
          component={RouterLink}
          to={AUTH_NAVIGATION_ITEMS.REGISTER.to}
          onClick={handleCloseAndBlur}
          variant="contained"
          sx={{
            boxShadow: 'none',
            textTransform: 'none',
            fontSize: '0.875rem',
            fontWeight: 'fontWeightSemiBold',
            padding: '10px 16px',
            borderRadius: `${theme.customSpacing.borderRadius.small}px`,
            backgroundColor: isRegisterActive ? 'primary.dark' : 'primary.main',
            color: 'primary.contrastText',
            '&:hover': {
              boxShadow: 'none',
              backgroundColor: 'primary.dark',
            },
          }}
        >
          {AUTH_NAVIGATION_ITEMS.REGISTER.label}
        </Button>
      </Stack>
    </Drawer>
  );
};

export default MobileDrawer;
