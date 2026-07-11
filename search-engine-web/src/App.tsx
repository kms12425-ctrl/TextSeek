import { lazy, Suspense } from 'react';
import { BrowserRouter, NavLink, Route, Routes } from 'react-router-dom';
import './index.css';

const HomePage = lazy(() => import('./pages/HomePage'));
const DocumentsPage = lazy(() => import('./pages/DocumentsPage'));
const DocumentDetailPage = lazy(() => import('./pages/DocumentDetailPage'));

function RouteFallback() {
  return (
    <div className="page-shell">
      <div className="loading-state">页面加载中...</div>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <header className="topbar">
          <div className="brand">全文搜索引擎</div>
          <nav className="topnav">
            <NavLink to="/" end className={({ isActive }) => `topnav-link${isActive ? ' is-active' : ''}`}>
              搜索
            </NavLink>
            <NavLink to="/documents" className={({ isActive }) => `topnav-link${isActive ? ' is-active' : ''}`}>
              文档管理
            </NavLink>
          </nav>
        </header>

        <main className="page-shell">
          <Suspense fallback={<RouteFallback />}>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/documents" element={<DocumentsPage />} />
              <Route path="/documents/:id" element={<DocumentDetailPage />} />
            </Routes>
          </Suspense>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
