export interface User {
  id: string;
  email: string;
  name: string;
  role: 'STUDENT' | 'ADMIN';
}

export interface LoginRequest {
  email: string;
  password?: string;
}

export interface LoginResponse {
  token: string;
  user?: User;
  userId?: number;
}

export interface DecodedJwt {
  id?: string;
  email?: string;
  name?: string;
  role?: 'STUDENT' | 'ADMIN';
  sub?: string;
  exp?: number;
  iat?: number;
  [key: string]: unknown;
}

export interface AuthContextState {
  currentUser: User | null;
  token: string | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
  refreshSession: () => Promise<void>;
}
