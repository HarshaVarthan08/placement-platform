import { useState } from 'react';
import axios from 'axios';
import { useForm, FormProvider } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { api } from '../../../services';
import { ROUTES } from '../../../constants';
import { FormTextField } from '../../../components/forms';
import { AuthCardWrapper } from '../../../components/shared';
import {
  Box,
  Button,
  InputAdornment,
  IconButton,
  Link,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import { User, Mail, Lock, Eye, EyeOff, AlertCircle } from 'lucide-react';

const registerSchema = z
  .object({
    name: z.string().min(1, 'Full Name is required'),
    email: z.string().min(1, 'Email is required').email('Invalid email address'),
    password: z
      .string()
      .min(8, 'Password must be at least 8 characters long')
      .regex(/[A-Za-z]/, 'Password must contain at least one letter')
      .regex(/[0-9]/, 'Password must contain at least one number'),
    confirmPassword: z.string().min(1, 'Please confirm your password'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  });

type RegisterFormData = z.infer<typeof registerSchema>;

export const Register = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const methods = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
  });

  const {
    handleSubmit,
    formState: { isSubmitting },
  } = methods;

  const onSubmit = async (data: RegisterFormData) => {
    setSubmitError(null);
    try {
      await api.post('/auth/register', {
        name: data.name,
        email: data.email,
        password: data.password,
      });

      // Redirect to login page on success, transferring state to trigger success alert
      navigate(ROUTES.GUEST.LOGIN, {
        state: { registrationSuccess: true },
      });
    } catch (error: unknown) {
      console.error('Registration error:', error);
      let errorMessage =
        'Registration failed. The server may be offline or the email is already in use.';
      if (axios.isAxiosError(error)) {
        errorMessage = error.response?.data?.message || error.message || errorMessage;
      } else if (error instanceof Error) {
        errorMessage = error.message;
      }
      setSubmitError(errorMessage);
    }
  };

  return (
    <AuthCardWrapper
      title="Start Your Journey"
      subtitle="Create a free student profile to get access to custom mock interviews, resume checks, and job tracking panels."
    >
      <Box sx={{ width: '100%', maxWidth: '400px', mx: 'auto' }}>
        <Typography variant="h3" gutterBottom sx={{ mb: 1, fontWeight: 'fontWeightBold' }}>
          Create Account
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Already have an account?{' '}
          <Link
            component={RouterLink}
            to={ROUTES.GUEST.LOGIN}
            color="primary"
            sx={{ fontWeight: 'medium' }}
          >
            Sign in
          </Link>
        </Typography>

        {submitError && (
          <Alert severity="error" icon={<AlertCircle size={20} />} sx={{ mb: 3, borderRadius: 1 }}>
            {submitError}
          </Alert>
        )}

        <FormProvider {...methods}>
          <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
            <FormTextField
              name="name"
              label="Full Name"
              type="text"
              autoComplete="name"
              disabled={isSubmitting}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <User size={18} />
                  </InputAdornment>
                ),
              }}
            />

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
              label="Password (min. 8 characters)"
              type={showPassword ? 'text' : 'password'}
              autoComplete="new-password"
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
                      onClick={() => setShowPassword((prev) => !prev)}
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

            <FormTextField
              name="confirmPassword"
              label="Confirm Password"
              type={showConfirmPassword ? 'text' : 'password'}
              autoComplete="new-password"
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
                      onClick={() => setShowConfirmPassword((prev) => !prev)}
                      edge="end"
                      aria-label="toggle password visibility"
                      disabled={isSubmitting}
                    >
                      {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

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
                minHeight: '48px', // Correct target tap area height
                mt: 1,
              }}
            >
              {isSubmitting ? <CircularProgress size={24} color="inherit" /> : 'Create Account'}
            </Button>
          </Box>
        </FormProvider>
      </Box>
    </AuthCardWrapper>
  );
};

export default Register;
