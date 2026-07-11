import type { ApiResult, DocInfo, DocumentDetail, SearchResponse } from './types';

const API_BASE = '/api';
const DEFAULT_TIMEOUT_MS = 10000;

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const controller = new AbortController();
  const timeoutId = window.setTimeout(() => controller.abort(), DEFAULT_TIMEOUT_MS);

  try {
    const response = await fetch(`${API_BASE}${path}`, {
      ...init,
      signal: controller.signal,
    });

    const payload = (await response.json()) as ApiResult<T>;
    if (!response.ok || payload.code >= 400) {
      throw new Error(payload.message || '请求失败');
    }

    return payload.data;
  } catch (error) {
    if (error instanceof DOMException && error.name === 'AbortError') {
      throw new Error('请求超时，请稍后重试');
    }
    throw error;
  } finally {
    window.clearTimeout(timeoutId);
  }
}

export async function search(
  q: string,
  mode = 'or',
  page = 0,
  size = 10,
): Promise<SearchResponse> {
  const params = new URLSearchParams({
    q,
    mode,
    page: String(page),
    size: String(size),
  });
  return request<SearchResponse>(`/search?${params.toString()}`);
}

export async function uploadDocument(file: File): Promise<DocInfo> {
  const form = new FormData();
  form.append('file', file);

  return request<DocInfo>('/documents/upload', {
    method: 'POST',
    body: form,
  });
}

export async function listDocuments(): Promise<DocInfo[]> {
  return request<DocInfo[]>('/documents');
}

export async function getDocument(docId: number): Promise<DocumentDetail> {
  return request<DocumentDetail>(`/documents/${docId}`);
}

export async function deleteDocument(docId: number): Promise<void> {
  await request<void>(`/documents/${docId}`, {
    method: 'DELETE',
  });
}

export async function getStatus(): Promise<Record<string, unknown>> {
  return request<Record<string, unknown>>('/status');
}
