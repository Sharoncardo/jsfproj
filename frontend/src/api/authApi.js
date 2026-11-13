import apiClient from './client';

export const authApi = {
  login: async (credentials) => {
    const response = await apiClient.post('/auth/login', credentials);
    return response.data;
  },

  register: async (userData) => {
    const response = await apiClient.post('/auth/register', userData);
    return response.data;
  },

  refresh: async (refreshToken) => {
    const response = await apiClient.post('/auth/refresh', { refreshToken });
    return response.data;
  },

  getCurrentUser: async () => {
    const response = await apiClient.get('/users/me');
    return response.data;
  },

  updateProfile: async (profileData) => {
    const response = await apiClient.put('/users/me', profileData);
    return response.data;
  },
};
