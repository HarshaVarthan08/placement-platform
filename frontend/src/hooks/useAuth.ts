import { useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import type { AuthContextState } from '../types';

/**
 * Custom hook providing access to AuthContext state and methods.
 */
export const useAuth = (): AuthContextState => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default useAuth;
