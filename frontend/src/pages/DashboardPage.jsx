import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts';
import { DashboardApi, DocumentsApi } from '../services/api.js';
import RiskSeal from '../components/RiskSeal.jsx';

const RISK_COLORS = { LOW: '#3f6d53', MEDIUM: '#c48a2e', HIGH: '#a6432e', UNKNOWN: '#55636f' };

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [recent, setRecent] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([DashboardApi.stats(), DocumentsApi.list()])
      .then(([s, docs]) => {
        setStats(s);
        setRecent(docs.slice(0, 5));
      })
      .catch((e) => setError(e?.response?.data?.message || 'Could not reach the backend API.'))
      .finally(() => setLoading(false));
  }, []);

  const pieData = stats
    ? Object.entries(stats.byRiskLevel || {}).map(([name, value]) => ({ name, value }))
    : [];

  return (
    <>
      <header className="page-header">
        <div className="eyebrow">Registry Overview</div>
        <h1 className="page-title">Dashboard</h1>
        <p className="page-sub">
          Automated risk auditing, document indexing, and grounded Q&A across your ingested deed archive.
        </p>
      </header>

      {error && <div className="error-banner">{error} — start the Spring Boot API on port 8080 and refresh.</div>}
      {loading && <div className="spinner" />}

      {stats && (
        <>
          <div className="stat-strip">
            <div className="stat-card">
              <div className="stat-value">{stats.totalDocuments}</div>
              <div className="stat-label">Documents Indexed</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">{stats.averageRiskScore ?? '—'}</div>
              <div className="stat-label">Avg. Risk Score</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">{stats.byRiskLevel?.HIGH ?? 0}</div>
              <div className="stat-label">High-Risk Deeds</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">{stats.byStatus?.ANALYZED ?? 0}</div>
              <div className="stat-label">Fully Analyzed</div>
            </div>
          </div>

          <div className="two-col">
            <div className="panel">
              <h3>Recently Ingested</h3>
              {recent.length === 0 ? (
                <div className="empty-state">No documents yet — upload a deed to get started.</div>
              ) : (
                <div className="stack" style={{ gap: 10 }}>
                  {recent.map((d) => (
                    <Link
                      key={d.id}
                      to={`/documents/${d.id}`}
                      style={{ display: 'flex', alignItems: 'center', gap: 14, textDecoration: 'none', color: 'inherit' }}
                    >
                      <RiskSeal level={d.riskLevel} small />
                      <div style={{ flex: 1 }}>
                        <div className="ledger-filename">{d.fileName}</div>
                        <div className="ledger-meta">{d.documentType || 'Unclassified'} · {d.status}</div>
                      </div>
                    </Link>
                  ))}
                </div>
              )}
            </div>

            <div className="panel">
              <h3>Risk Distribution</h3>
              {pieData.length === 0 ? (
                <div className="empty-state">No risk data yet.</div>
              ) : (
                <ResponsiveContainer width="100%" height={220}>
                  <PieChart>
                    <Pie data={pieData} dataKey="value" nameKey="name" innerRadius={50} outerRadius={80} paddingAngle={3}>
                      {pieData.map((entry) => (
                        <Cell key={entry.name} fill={RISK_COLORS[entry.name] || '#7e8fa0'} />
                      ))}
                    </Pie>
                    <Tooltip contentStyle={{ background: '#12212f', border: '1px solid rgba(233,228,216,0.1)', borderRadius: 8 }} />
                  </PieChart>
                </ResponsiveContainer>
              )}
            </div>
          </div>
        </>
      )}
    </>
  );
}
