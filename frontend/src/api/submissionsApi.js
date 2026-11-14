
import apiClient from './client';

export const submissionsApi = {
  submitSolution: async (submissionData) => {
    const response = await apiClient.post(
      '/submissions',
      submissionData
    );
    return response.data;
  },

  getSubmissionsByProblem: async (problemId) => {
    const response = await apiClient.get(
      `/submissions/problem/${problemId}`
    );
    return response.data;
  },

  getSubmissionById: async (submissionId) => {
    const response = await apiClient.get(
      `/submissions/${submissionId}`
    );
    return response.data;
  },
};
