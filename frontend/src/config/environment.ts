interface Environment {
  apiUrl: string;
  appName: string;
  env: 'development' | 'production' | 'test';
  isDev: boolean;
  isProd: boolean;
}

export const env: Environment = {
  apiUrl: import.meta.env.VITE_API_URL || 'http://localhost:5000/api',
  appName: import.meta.env.VITE_APP_NAME || 'AI Placement Platform',
  env: (import.meta.env.VITE_APP_ENV as Environment['env']) || 'development',
  isDev: import.meta.env.DEV,
  isProd: import.meta.env.PROD,
};
