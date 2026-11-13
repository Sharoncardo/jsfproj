import apiClient from './client';

export const notesApi = {
  getNotes: async (problemId = null) => {
    const params = problemId ? { problemId } : {};
    const response = await apiClient.get('/notes', { params });
    return response.data;
  },

  getNoteById: async (id) => {
    const response = await apiClient.get(`/notes/${id}`);
    return response.data;
  },

  createNote: async (noteData) => {
    const response = await apiClient.post('/notes', noteData);
    return response.data;
  },

  updateNote: async (id, noteData) => {
    const response = await apiClient.put(`/notes/${id}`, noteData);
    return response.data;
  },

  updatePosition: async (id, positionData) => {
    const response = await apiClient.patch(`/notes/${id}/position`, positionData);
    return response.data;
  },

  deleteNote: async (id) => {
    const response = await apiClient.delete(`/notes/${id}`);
    return response.data;
  },
};
