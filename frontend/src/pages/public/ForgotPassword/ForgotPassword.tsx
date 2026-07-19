import { Link as RouterLink } from 'react-router-dom';
import { ROUTES } from '../../../constants';
import { AuthCardWrapper } from '../../../components/shared';
import { Box, Button, Typography } from '@mui/material';
import { MailQuestion } from 'lucide-react';

export const ForgotPassword = () => {
  return (
    <AuthCardWrapper
      title="Reset Password"
      subtitle="Forgot your credentials? Enter your registered email address to verify your profile and retrieve access options."
    >
      <Box
        sx={{
          width: '100%',
          maxWidth: '400px',
          mx: 'auto',
          textAlign: 'center',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          py: 2,
        }}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: 72,
            height: 72,
            borderRadius: '50%',
            backgroundColor: 'rgba(0, 102, 255, 0.08)',
            color: 'primary.main',
            mb: 3,
          }}
        >
          <MailQuestion size={36} strokeWidth={2} />
        </Box>

        <Typography variant="h3" gutterBottom sx={{ mb: 1.5, fontWeight: 'fontWeightBold' }}>
          Coming Soon
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 4, lineHeight: 1.6 }}>
          This feature will be available in a future sprint. Once connected, you will be able to
          receive recovery verification links directly to your registered mailbox.
        </Typography>

        <Button
          component={RouterLink}
          to={ROUTES.GUEST.LOGIN}
          variant="contained"
          color="primary"
          size="large"
          fullWidth
          sx={{
            py: 1.5,
            fontWeight: 'fontWeightBold',
            textTransform: 'none',
            minHeight: '48px',
          }}
        >
          Back to Sign In
        </Button>
      </Box>
    </AuthCardWrapper>
  );
};

export default ForgotPassword;
