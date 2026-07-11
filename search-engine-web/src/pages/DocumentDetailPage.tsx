import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getDocument } from '../api/search';
import type { DocumentDetail } from '../api/types';

export default function DocumentDetailPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [document, setDocument] = useState<DocumentDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadDocument = async () => {
      if (!id) {
        setError('文档 ID 无效');
        setLoading(false);
        return;
      }

      setLoading(true);
      setError(null);
      try {
        const data = await getDocument(Number(id));
        setDocument(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : '获取文档详情失败');
      } finally {
        setLoading(false);
      }
    };

    void loadDocument();
  }, [id]);

  return (
    <section className="section-stack">
      <button type="button" className="secondary-button back-button" onClick={() => navigate('/documents')}>
        返回文档列表
      </button>

      {loading ? (
        <div className="table-card">
          <div className="loading-state">文档加载中...</div>
        </div>
      ) : error ? (
        <div className="banner banner-error">{error}</div>
      ) : document ? (
        <>
          <div className="detail-card">
            <div className="detail-header">
              <h2 className="page-title detail-title">{document.docName}</h2>
              <span className="pill pill-blue">{document.termCount} 个词条</span>
            </div>

            <dl className="detail-grid">
              <div>
                <dt>文档 ID</dt>
                <dd>{document.docId}</dd>
              </div>
              <div>
                <dt>文件路径</dt>
                <dd>
                  <code>{document.docPath}</code>
                </dd>
              </div>
            </dl>
          </div>

          <div className="detail-card">
            <h3 className="section-title">文档内容</h3>
            <pre className="document-content">{document.content || '文档内容为空'}</pre>
          </div>
        </>
      ) : null}
    </section>
  );
}
