import React from 'react';
import { Box } from '@mui/material';
import { Outlet } from 'react-router-dom';
import { useUI } from '../hooks/useUI';

export const DashboardLayout: React.FC = () => {
  const { sidebarOpen } = useUI();

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
        Future Sidebar slot wrapper.
        Reserved structural space:
        - md and larger: 260px if sidebarOpen, else 72px width.
        - xs and sm (mobile/tablet): 0 width (hidden, drawer overlay used in future).
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
        }}
      />

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
        {/* 
          Future Topbar slot wrapper.
          Reserved structural space: 64px height.
        */}
        <Box
          component="header"
          sx={{
            height: 64,
            minHeight: 64,
            width: '100%',
            borderBottom: '1px solid',
            borderColor: 'divider',
            display: 'flex',
            alignItems: 'center',
            px: 3,
          }}
        />

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
