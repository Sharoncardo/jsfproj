
import apiClient from './client';

export const hintsApi = {
  getHints: async (problemId) => {
    const response = await apiClient.get(
      `/problems/${problemId}/hints`
    );
    return response.data;
  },

  unlockNextHint: async (problemId) => {
    const response = await apiClient.post(
      `/problems/${problemId}/hints/unlock`
    );
    return response.data;
  },
};
