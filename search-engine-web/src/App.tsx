import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ConfigProvider, Layout, theme } from 'antd';
import HomePage from './pages/HomePage';
import DocumentsPage from './pages/DocumentsPage';

const { Header, Content } = Layout;

function App()
{
  return (
    <ConfigProvider
      theme={{
        algorithm: theme.defaultAlgorithm,
        token: { colorPrimary: '#1677ff', borderRadius: 8 },
      }}
    >
      <BrowserRouter>
        <Layout style={{ minHeight: '100vh' }}>
          <Header
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 24,
              background: '#001529',
            }}
          >
            <h1 style={{ color: '#fff', margin: 0, fontSize: 20, whiteSpace: 'nowrap' }}>
              🔍 全文搜索引擎
            </h1>
            <nav style={{ display: 'flex', gap: 16 }}>
              <a href="/" style={{ color: '#fff', textDecoration: 'none', fontSize: 15 }}>搜索</a>
              <a href="/documents" style={{ color: '#fff', textDecoration: 'none', fontSize: 15 }}>文档管理</a>
            </nav>
          </Header>
          <Content style={{ padding: '32px 48px', maxWidth: 1200, margin: '0 auto', width: '100%' }}>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/documents" element={<DocumentsPage />} />
            </Routes>
          </Content>
        </Layout>
      </BrowserRouter>
    </ConfigProvider>
  );
}

export default App;
