import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { DocumentsApi } from '../services/api.js';
import RiskSeal from '../components/RiskSeal.jsx';

export default function DocumentsPage() {
  const [docs, setDocs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    DocumentsApi.list()
      .then(setDocs)
      .catch((e) => setError(e?.response?.data?.message || 'Could not reach the backend API.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <header className="page-header">
        <div className="eyebrow">Registry</div>
        <h1 className="page-title">Document Ledger</h1>
        <p className="page-sub">Every deed processed by the analyzer, indexed with its extracted schema and audit outcome.</p>
      </header>

      {error && <div className="error-banner">{error}</div>}
      {loading && <div className="spinner" />}

      {!loading && docs.length === 0 && !error && (
        <div className="empty-state">
          No documents yet. <Link to="/upload" style={{ color: 'var(--brass-bright)' }}>Upload your first deed</Link>.
        </div>
      )}

      {docs.length > 0 && (
        <div className="ledger">
          <div className="ledger-row header">
            <span></span>
            <span>File / Parties</span>
            <span className="ledger-cell secondary">Type</span>
            <span className="ledger-cell secondary">Survey No.</span>
            <span>Status</span>
            <span>Risk</span>
          </div>
          {docs.map((d) => (
            <Link className="ledger-row" key={d.id} to={`/documents/${d.id}`}>
              <RiskSeal level={d.riskLevel} small />
              <div>
                <div className="ledger-filename">{d.fileName}</div>
                <div className="ledger-meta">
                  {d.granterName ? `${d.granterName} → ${d.granteeName || '—'}` : 'Parties pending extraction'}
                </div>
              </div>
              <div className="ledger-cell secondary ledger-meta">{d.documentType || '—'}</div>
              <div className="ledger-cell secondary ledger-meta">{d.surveyNumber || '—'}</div>
              <div><span className="status-pill">{d.status}</span></div>
              <div className="ledger-meta">{d.riskScore ?? '—'}</div>
            </Link>
          ))}
        </div>
      )}
    </>
  );
}
