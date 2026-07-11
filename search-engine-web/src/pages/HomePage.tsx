import { useState } from 'react';
import { search } from '../api/search';
import type { HitItem, SearchResponse } from '../api/types';

function escapeRegex(term: string) {
  return term.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function renderHighlightedSnippet(hit: HitItem) {
  if (!hit.snippet) {
    return null;
  }

  const terms = Object.keys(hit.termFreqMap).filter(Boolean);
  if (terms.length === 0) {
    return <span>{hit.snippet}</span>;
  }

  const pattern = new RegExp(`(${terms.map(escapeRegex).join('|')})`, 'gi');
  const parts = hit.snippet.split(pattern);

  return parts.map((part, index) => {
    const matched = terms.some((term) => term.toLowerCase() === part.toLowerCase());
    return matched ? <mark key={`${hit.docId}-${index}`}>{part}</mark> : <span key={`${hit.docId}-${index}`}>{part}</span>;
  });
}

export default function HomePage() {
  const [keyword, setKeyword] = useState('');
  const [mode, setMode] = useState<'or' | 'and'>('or');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<SearchResponse | null>(null);
  const [page, setPage] = useState(0);
  const pageSize = 10;
  const totalPages = result ? Math.max(1, Math.ceil(result.totalHits / pageSize)) : 0;

  const doSearch = async (nextPage = 0) => {
    if (!keyword.trim()) {
      return;
    }

    setLoading(true);
    try {
      const response = await search(keyword.trim(), mode, nextPage, pageSize);
      setResult(response);
      setPage(nextPage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="section-stack">
      <div>
        <h2 className="page-title">全文搜索</h2>
        <p className="page-subtitle">输入关键词，支持 AND / OR 两种匹配模式。</p>
      </div>

      <div className="search-bar">
        <input
          className="text-input"
          placeholder="输入搜索关键词，多词请用空格分隔"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              void doSearch(0);
            }
          }}
        />
        <button className="primary-button" type="button" onClick={() => void doSearch(0)} disabled={loading}>
          {loading ? '搜索中...' : '搜索'}
        </button>
      </div>

      <div className="segmented-control" role="radiogroup" aria-label="搜索模式">
        <button
          type="button"
          className={`segment-button${mode === 'or' ? ' is-active' : ''}`}
          onClick={() => setMode('or')}
        >
          OR（包含任一关键词）
        </button>
        <button
          type="button"
          className={`segment-button${mode === 'and' ? ' is-active' : ''}`}
          onClick={() => setMode('and')}
        >
          AND（包含全部关键词）
        </button>
      </div>

      {result && (
        <p className="status-text">
          找到 {result.totalHits} 条结果（第 {result.page + 1} 页，共 {totalPages} 页）
        </p>
      )}

      {result && result.hits.length === 0 && <div className="empty-state">没有找到匹配文档</div>}

      <div className="card-list">
        {result?.hits.map((hit) => (
          <article key={hit.docId} className="result-card">
            <div className="result-header">
              <strong>{hit.docName}</strong>
              <span className="pill pill-blue">得分 {hit.score.toFixed(1)}</span>
            </div>

            <div className="snippet-text">{renderHighlightedSnippet(hit)}</div>

            <div className="tag-row">
              {Object.entries(hit.termFreqMap).map(([term, freq]) => (
                <span key={term} className="pill pill-green">
                  {term} × {freq}
                </span>
              ))}
            </div>
          </article>
        ))}
      </div>

      {result && result.totalHits > pageSize && (
        <div className="pager">
          <button type="button" className="secondary-button" disabled={page === 0} onClick={() => void doSearch(page - 1)}>
            上一页
          </button>
          <span className="status-text">
            {page + 1} / {totalPages}
          </span>
          <button
            type="button"
            className="secondary-button"
            disabled={page + 1 >= totalPages}
            onClick={() => void doSearch(page + 1)}
          >
            下一页
          </button>
        </div>
      )}
    </section>
  );
}
