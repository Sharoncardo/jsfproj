
import apiClient from './client';

export const playbookApi = {
  getPlaybook: async () => {
    const response = await apiClient.get('/playbook');
    return response.data;
  },
};
