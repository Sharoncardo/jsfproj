
import apiClient from './client';

export const problemsApi = {
  getTopics: async () => {
    const response = await apiClient.get('/problems/topics');
    return response.data;
  },

  getProblems: async (params = {}) => {
    const response = await apiClient.get('/problems', { params });
    return response.data;
  },

  getProblemById: async (id) => {
    const response = await apiClient.get(`/problems/${id}`);
    return response.data;
  },

  getProblemBySlug: async (slug) => {
    const response = await apiClient.get(`/problems/slug/${slug}`);
    return response.data;
  },
};
