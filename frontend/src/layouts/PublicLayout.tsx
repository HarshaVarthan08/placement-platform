import { Box } from '@mui/material';
import { Outlet } from 'react-router-dom';
import { Navbar } from '../components/navigation';
import { Footer } from '../components/shared';

export const PublicLayout = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        backgroundColor: 'background.default',
      }}
    >
      {/* Navigation Header */}
      <Navbar />

      {/* Main Content Area */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
          width: '100%',
        }}
      >
        <Outlet />
      </Box>

      {/* Footer Section */}
      <Footer />
    </Box>
  );
};

export default PublicLayout;
