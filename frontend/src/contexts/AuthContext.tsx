/* eslint-disable react-refresh/only-export-components */
import React, { createContext, useState, useEffect, useCallback } from 'react';
import type { User, LoginRequest, LoginResponse, AuthContextState } from '../types';
import { decodeToken, isTokenExpired } from '../utils';
import { api, registerLogoutCallback } from '../services';

export const AuthContext = createContext<AuthContextState | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);

  // Standard logout method: clears local storage and resets authentication state
  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setCurrentUser(null);
    setToken(null);
    setIsAuthenticated(false);
    setLoading(false);
  }, []);

  // Restores session by validating JWT token and decoding identity claims
  const refreshSession = useCallback(async () => {
    try {
      const storedToken = localStorage.getItem('token');
      if (!storedToken) {
        logout();
        return;
      }

      if (isTokenExpired(storedToken)) {
        logout();
        return;
      }

      const decoded = decodeToken(storedToken);
      if (!decoded) {
        logout();
        return;
      }

      const user: User = {
        id: decoded.id || decoded.sub || '',
        email: decoded.email || decoded.sub || '',
        name: decoded.name || '',
        role: decoded.role || 'STUDENT',
      };

      setToken(storedToken);
      setCurrentUser(user);
      setIsAuthenticated(true);
    } catch (error) {
      console.error('Session restoration failed:', error);
      logout();
    } finally {
      setLoading(false);
    }
  }, [logout]);

  // Performs credentials login, stores token, and updates context state
  const login = useCallback(
    async (credentials: LoginRequest) => {
      setLoading(true);
      try {
        const response = await api.post<LoginResponse>('/auth/login', credentials);
        const { token: receivedToken, user: responseUser, userId } = response.data;

        if (!receivedToken) {
          throw new Error('Malformed token response from API');
        }

        const decoded = decodeToken(receivedToken);
        const user: User = responseUser || {
          id: decoded?.id || decoded?.sub || String(userId || ''),
          email: decoded?.email || decoded?.sub || credentials.email,
          name: decoded?.name || '',
          role: decoded?.role || 'STUDENT',
        };

        localStorage.setItem('token', receivedToken);
        setToken(receivedToken);
        setCurrentUser(user);
        setIsAuthenticated(true);
      } catch (error) {
        logout();
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [logout],
  );

  // Synchronize on initialization
  useEffect(() => {
    registerLogoutCallback(logout);

    let isMounted = true;
    const initialize = async () => {
      // Yield to the event loop so it is not synchronous
      await Promise.resolve();
      if (isMounted) {
        await refreshSession();
      }
    };

    initialize();

    return () => {
      isMounted = false;
    };
  }, [logout, refreshSession]);

  return (
    <AuthContext.Provider
      value={{
        currentUser,
        token,
        isAuthenticated,
        loading,
        login,
        logout,
        refreshSession,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
