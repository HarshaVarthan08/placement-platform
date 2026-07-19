import { jwtDecode } from 'jwt-decode';
import type { DecodedJwt } from '../types';

/**
 * Safely decodes a JWT token. Returns null if the token is malformed.
 */
export const decodeToken = (token: string): DecodedJwt | null => {
  if (!token) return null;
  try {
    return jwtDecode<DecodedJwt>(token);
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return null;
  }
};

/**
 * Checks if a JWT token is expired.
 */
export const isTokenExpired = (token: string): boolean => {
  if (!token) return true;
  const decoded = decodeToken(token);
  if (!decoded || typeof decoded.exp === 'undefined') {
    return true;
  }
  const currentTime = Date.now() / 1000;
  return decoded.exp < currentTime;
};
