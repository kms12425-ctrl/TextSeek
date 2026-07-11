import axios from 'axios';
import type { ApiResult, SearchResponse, DocInfo } from './types';

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
});

export async function search(
    q: string,
    mode = 'or',
    page = 0,
    size = 10,
): Promise<SearchResponse>
{
    const res = await api.get<ApiResult<SearchResponse>>('/search', {
        params: { q, mode, page, size },
    });
    return res.data.data;
}

export async function uploadDocument(file: File): Promise<DocInfo>
{
    const form = new FormData();
    form.append('file', file);
    const res = await api.post<ApiResult<DocInfo>>('/documents/upload', form);
    return res.data.data;
}

export async function listDocuments(): Promise<DocInfo[]>
{
    const res = await api.get<ApiResult<DocInfo[]>>('/documents');
    return res.data.data;
}

export async function deleteDocument(docId: number): Promise<void>
{
    await api.delete(`/documents/${docId}`);
}

export async function getStatus(): Promise<Record<string, unknown>>
{
    const res = await api.get<ApiResult<Record<string, unknown>>>('/status');
    return res.data.data;
}
