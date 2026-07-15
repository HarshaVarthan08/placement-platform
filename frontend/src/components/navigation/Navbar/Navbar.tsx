import { useState } from 'react';
import { AppBar, Container, Toolbar, useTheme } from '@mui/material';
import { LAYOUT } from '../../../constants';
import { useScrolled } from '../../../hooks';
import NavbarLogo from './NavbarLogo';
import DesktopNavigation from './DesktopNavigation';
import MobileNavigation from './MobileNavigation';
import MobileDrawer from './MobileDrawer';
import { alpha } from '@mui/material/styles';

export const Navbar = () => {
  const theme = useTheme();
  const isScrolled = useScrolled(0);
  const [drawerOpen, setDrawerOpen] = useState(false);

  const handleDrawerOpen = () => setDrawerOpen(true);
  const handleDrawerClose = () => setDrawerOpen(false);

  return (
    <AppBar
      position="sticky"
      elevation={0}
      component="header"
      sx={{
        backgroundColor: isScrolled ? alpha(theme.palette.background.paper, 0.9) : 'transparent',
        backdropFilter: isScrolled ? 'blur(8px)' : 'none',
        borderBottom: '1px solid',
        borderColor: isScrolled ? 'divider' : 'transparent',
        boxShadow: isScrolled ? theme.customShadows.level1 : 'none',
        zIndex: LAYOUT.NAVBAR_Z_INDEX,
        // Override default AppBar white text in dark settings or default setups
        color: 'text.primary',
        transition: theme.transitions.create(['background-color', 'box-shadow', 'border-color'], {
          easing: theme.transitions.easing.easeInOut,
          duration: theme.transitions.duration.standard,
        }),
      }}
    >
      <Container
        maxWidth="lg"
        sx={{
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        <Toolbar
          disableGutters
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            height: {
              xs: LAYOUT.NAVBAR_HEIGHT_MOBILE,
              md: LAYOUT.NAVBAR_HEIGHT_DESKTOP,
            },
          }}
        >
          {/* Logo Section */}
          <NavbarLogo />

          {/* Desktop Navigation */}
          <DesktopNavigation />

          {/* Mobile Navigation Hamburger Menu */}
          <MobileNavigation onOpen={handleDrawerOpen} open={drawerOpen} />
        </Toolbar>
      </Container>

      {/* Mobile Drawer */}
      <MobileDrawer open={drawerOpen} onClose={handleDrawerClose} />
    </AppBar>
  );
};

export default Navbar;
