import apiClient from './client';

export const progressApi = {
  getSummary: async () => {
    const response = await apiClient.get('/progress/summary');
    return response.data;
  },

  recordAttempt: async (attemptData) => {
    const response = await apiClient.post('/progress', attemptData);
    return response.data;
  },

  getProgressList: async (params = {}) => {
    const response = await apiClient.get('/progress', { params });
    return response.data;
  },
};
