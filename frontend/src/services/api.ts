import axios from 'axios';

// Singleton axios instance
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:5000/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Callback reference to logout user dynamically (decoupled from AuthContext)
let logoutCallback: (() => void) | null = null;

/**
 * Registers a logout callback to be executed on authentication failures (e.g., HTTP 401).
 */
export const registerLogoutCallback = (callback: () => void) => {
  logoutCallback = callback;
};

// Request interceptor: Attach JWT token dynamically
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// Response interceptor: Catch 401 errors and trigger logout callback
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      if (logoutCallback) {
        logoutCallback();
      } else {
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  },
);

export default api;
