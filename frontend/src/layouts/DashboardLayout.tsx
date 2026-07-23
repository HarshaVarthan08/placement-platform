import React from 'react';
import { Box, Drawer } from '@mui/material';
import { Outlet } from 'react-router-dom';
import { useUI } from '../hooks/useUI';
import { Sidebar, Topbar } from '../components/dashboard';

export const DashboardLayout: React.FC = () => {
  const { sidebarOpen, mobileSidebarOpen, closeMobileSidebar } = useUI();

  return (
    <Box
      sx={{
        display: 'flex',
        minHeight: '100vh',
        maxHeight: '100vh',
        width: '100%',
        overflow: 'hidden',
        backgroundColor: 'background.default',
      }}
    >
      {/* 
        Persistent Desktop/Laptop Sidebar.
        - md and larger: 260px if sidebarOpen, else 72px width.
        - xs and sm (mobile/tablet): 0 width (hidden, temporary drawer overlay used instead).
      */}
      <Box
        component="aside"
        sx={{
          width: {
            xs: 0,
            md: sidebarOpen ? 260 : 72,
          },
          minWidth: {
            xs: 0,
            md: sidebarOpen ? 260 : 72,
          },
          transition:
            'width 225ms cubic-bezier(0.4, 0, 0.2, 1), min-width 225ms cubic-bezier(0.4, 0, 0.2, 1)',
          overflow: 'hidden',
          display: { xs: 'none', md: 'block' },
          height: '100vh',
        }}
      >
        <Sidebar />
      </Box>

      {/* Mobile/Tablet Sidebar Drawer Overlay */}
      <Drawer
        variant="temporary"
        open={mobileSidebarOpen}
        onClose={closeMobileSidebar}
        ModalProps={{
          keepMounted: true, // Better open performance on mobile viewports.
        }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': {
            boxSizing: 'border-box',
            width: 260,
            border: 'none',
          },
        }}
      >
        <Sidebar isMobileDrawer />
      </Drawer>

      {/* Main workspace area */}
      <Box
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
          height: '100vh',
          maxHeight: '100vh',
          overflow: 'hidden',
          width: '100%',
        }}
      >
        {/* Sticky Topbar Header */}
        <Topbar />

        {/* Main content body container */}
        <Box
          component="main"
          id="main-content"
          tabIndex={-1}
          sx={{
            flexGrow: 1,
            overflowY: 'auto',
            overflowX: 'hidden',
            p: { xs: 1.5, sm: 2, md: 3 },
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default DashboardLayout;
