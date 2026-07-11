import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { deleteDocument, listDocuments, uploadDocument } from '../api/search';
import type { DocInfo } from '../api/types';

export default function DocumentsPage() {
  const navigate = useNavigate();
  const [docs, setDocs] = useState<DocInfo[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const fetchDocs = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listDocuments();
      setDocs(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : '获取文档列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void fetchDocs();
  }, []);

  const handleUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    setMessage(null);
    setError(null);
    try {
      await uploadDocument(file);
      setMessage(`上传成功: ${file.name}`);
      await fetchDocs();
    } catch (err) {
      setError(err instanceof Error ? err.message : `上传失败: ${file.name}`);
    } finally {
      event.target.value = '';
    }
  };

  const handleDelete = async (docId: number) => {
    const confirmed = window.confirm('确定删除该文档吗？');
    if (!confirmed) {
      return;
    }

    setMessage(null);
    setError(null);
    try {
      await deleteDocument(docId);
      setMessage('已删除');
      await fetchDocs();
    } catch (err) {
      setError(err instanceof Error ? err.message : '删除失败');
    }
  };

  return (
    <section className="section-stack">
      <div>
        <h2 className="page-title">文档管理</h2>
        <p className="page-subtitle">上传、查看和删除已经建立索引的文本文件。</p>
      </div>

      <div className="toolbar-card">
        <label className="primary-button file-button">
          上传 .txt 文档
          <input type="file" accept=".txt" onChange={handleUpload} hidden />
        </label>
        <button type="button" className="secondary-button" onClick={() => void fetchDocs()} disabled={loading}>
          刷新列表
        </button>
      </div>

      {message && <div className="banner banner-success">{message}</div>}
      {error && <div className="banner banner-error">{error}</div>}

      <div className="table-card">
        {loading ? (
          <div className="loading-state">文档加载中...</div>
        ) : docs.length === 0 ? (
          <div className="empty-state">暂无文档，请先上传 .txt 文件</div>
        ) : (
          <div className="table-wrap">
            <table className="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>文件名</th>
                  <th>词条数</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                {docs.map((doc) => (
                  <tr key={doc.docId}>
                    <td>{doc.docId}</td>
                    <td className="cell-ellipsis" title={doc.docName}>
                      {doc.docName}
                    </td>
                    <td>{doc.termCount}</td>
                    <td>
                      <div className="inline-actions">
                        <button type="button" className="link-button" onClick={() => navigate(`/documents/${doc.docId}`)}>
                          查看
                        </button>
                        <button type="button" className="link-button danger-text" onClick={() => void handleDelete(doc.docId)}>
                          删除
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </section>
  );
}
