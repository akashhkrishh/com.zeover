// lib/axios.ts
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8938/api',
  headers: {
    'Content-Type': 'application/json',
  },
  // Optional: enable if you use cookies/auth
  // withCredentials: true,
});

// Add X-Frontend-Secret header if environment variable is set
api.interceptors.request.use(
  (config) => {
    if (!config.headers) {
      config.headers = {} as import('axios').AxiosRequestHeaders;
    }
    const secret = process.env.NEXT_PUBLIC_BACKEND_SECRET;
    if (secret) {
      config.headers['X-Frontend-Secret'] = "my-frontend-secret";
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
