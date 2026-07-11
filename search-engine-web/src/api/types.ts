// 搜索引擎前端类型定义

export interface HitItem
{
    docId: number;
    docName: string;
    docPath: string;
    score: number;
    termFreqMap: Record<string, number>;
    snippet: string;
}

export interface SearchResponse
{
    totalHits: number;
    page: number;
    size: number;
    hits: HitItem[];
}

export interface DocInfo
{
    docId: number;
    docName: string;
    docPath: string;
    termCount: number;
}

export interface ApiResult<T>
{
    code: number;
    message: string;
    data: T;
}
