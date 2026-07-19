import { useState } from 'react';
import { useForm, FormProvider } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Link as RouterLink, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../hooks';
import { ROUTES } from '../../../constants';
import { FormTextField } from '../../../components/forms';
import { AuthCardWrapper } from '../../../components/shared';
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  InputAdornment,
  IconButton,
  Link,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import { Mail, Lock, Eye, EyeOff, AlertCircle } from 'lucide-react';

const loginSchema = z.object({
  email: z.string().min(1, 'Email is required').email('Invalid email address'),
  password: z.string().min(1, 'Password is required'),
  rememberMe: z.boolean().optional(),
});

import axios from 'axios';

interface LocationState {
  registrationSuccess?: boolean;
  from?: {
    pathname: string;
  };
}

type LoginFormData = z.infer<typeof loginSchema>;

export const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [showPassword, setShowPassword] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const state = location.state as LocationState | null;
  const [successMessage, setSuccessMessage] = useState<string | null>(
    state?.registrationSuccess ? 'Registration successful! Please sign in below.' : null,
  );

  const methods = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
      rememberMe: false,
    },
  });

  const {
    handleSubmit,
    formState: { isSubmitting },
  } = methods;

  // Retrieve destination from route state, fallback to private dashboard
  const from = state?.from?.pathname || ROUTES.PRIVATE.DASHBOARD;

  const onSubmit = async (data: LoginFormData) => {
    setSubmitError(null);
    setSuccessMessage(null);
    try {
      await login({
        email: data.email,
        password: data.password,
      });
      // Navigate on success
      navigate(from, { replace: true });
    } catch (error: unknown) {
      console.error('Login error:', error);
      let errorMessage = 'Network error. Please verify your connection or try again later.';
      if (axios.isAxiosError(error)) {
        errorMessage = error.response?.data?.message || error.message || errorMessage;
      } else if (error instanceof Error) {
        errorMessage = error.message;
      }
      setSubmitError(errorMessage);
    }
  };

  const handleTogglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <AuthCardWrapper
      title="Welcome Back"
      subtitle="Sign in to your account to resume your placement journey and access AI analytics reviews."
    >
      <Box sx={{ width: '100%', maxWidth: '400px', mx: 'auto' }}>
        <Typography variant="h3" gutterBottom sx={{ mb: 1, fontWeight: 'fontWeightBold' }}>
          Sign In
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          New here?{' '}
          <Link
            component={RouterLink}
            to={ROUTES.GUEST.REGISTER}
            color="primary"
            sx={{ fontWeight: 'medium' }}
          >
            Create an account
          </Link>
        </Typography>

        {successMessage && (
          <Alert severity="success" sx={{ mb: 3, borderRadius: 1 }}>
            {successMessage}
          </Alert>
        )}

        {submitError && (
          <Alert severity="error" icon={<AlertCircle size={20} />} sx={{ mb: 3, borderRadius: 1 }}>
            {submitError}
          </Alert>
        )}

        <FormProvider {...methods}>
          <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
            <FormTextField
              name="email"
              label="Email Address"
              type="email"
              autoComplete="email"
              disabled={isSubmitting}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Mail size={18} />
                  </InputAdornment>
                ),
              }}
            />

            <FormTextField
              name="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              disabled={isSubmitting}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Lock size={18} />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={handleTogglePassword}
                      edge="end"
                      aria-label="toggle password visibility"
                      disabled={isSubmitting}
                    >
                      {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                mb: 3,
                mt: -1,
              }}
            >
              <FormControlLabel
                control={
                  <Checkbox
                    {...methods.register('rememberMe')}
                    color="primary"
                    disabled={isSubmitting}
                  />
                }
                label={
                  <Typography variant="body2" sx={{ userSelect: 'none' }}>
                    Remember me
                  </Typography>
                }
              />
              <Link
                component={RouterLink}
                to={ROUTES.GUEST.FORGOT_PASSWORD}
                variant="body2"
                color="primary"
                sx={{ fontWeight: 'medium' }}
              >
                Forgot Password?
              </Link>
            </Box>

            <Button
              type="submit"
              variant="contained"
              color="primary"
              size="large"
              fullWidth
              disabled={isSubmitting}
              sx={{
                py: 1.5,
                fontWeight: 'fontWeightBold',
                textTransform: 'none',
                minHeight: '48px', // Ensure correct tap target height
              }}
            >
              {isSubmitting ? <CircularProgress size={24} color="inherit" /> : 'Sign In'}
            </Button>
          </Box>
        </FormProvider>
      </Box>
    </AuthCardWrapper>
  );
};

export default Login;
