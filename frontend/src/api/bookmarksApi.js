import apiClient from './client';

export const bookmarksApi = {
  getBookmarks: async (page = 0, size = 20) => {
    const response = await apiClient.get('/bookmarks', { params: { page, size } });
    return response.data;
  },

  createBookmark: async (bookmarkData) => {
    const response = await apiClient.post('/bookmarks', bookmarkData);
    return response.data;
  },

  updateBookmark: async (id, bookmarkData) => {
    const response = await apiClient.put(`/bookmarks/${id}`, bookmarkData);
    return response.data;
  },

  deleteBookmark: async (id) => {
    const response = await apiClient.delete(`/bookmarks/${id}`);
    return response.data;
  },

  checkBookmark: async (problemId) => {
    const response = await apiClient.get('/bookmarks/check', { params: { problemId } });
    return response.data;
  },
};
