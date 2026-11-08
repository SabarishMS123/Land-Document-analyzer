import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 60000, // LLM extraction + audit can take a while
});

export const DocumentsApi = {
  upload: (file, onUploadProgress) => {
    const form = new FormData();
    form.append('file', file);
    return api.post('/documents/upload', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress,
    }).then(r => r.data);
  },
  list: () => api.get('/documents').then(r => r.data),
  get: (id) => api.get(`/documents/${id}`).then(r => r.data),
  riskFlags: (id) => api.get(`/documents/${id}/risk-flags`).then(r => r.data),
  rawText: (id) => api.get(`/documents/${id}/raw-text`).then(r => r.data),
  remove: (id) => api.delete(`/documents/${id}`),
};

export const QnaApi = {
  ask: (documentId, question) =>
    api.post(`/documents/${documentId}/qna`, { question }).then(r => r.data),
  history: (documentId) =>
    api.get(`/documents/${documentId}/qna`).then(r => r.data),
};

export const DashboardApi = {
  stats: () => api.get('/dashboard/stats').then(r => r.data),
};

export default api;
